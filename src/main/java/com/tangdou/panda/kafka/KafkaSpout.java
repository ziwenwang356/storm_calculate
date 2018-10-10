package com.tangdou.panda.kafka;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class KafkaSpout implements IRichSpout {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(KafkaSpout.class);

    protected SpoutOutputCollector collector;

    private long lastUpdateMs;
    PartitionCoordinator coordinator;

    private KafkaSpoutConfig config;

    private ZkState zkState;

    public KafkaSpout(KafkaSpoutConfig config) {
        this.config = config;
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        zkState = new ZkState(conf, config);
        coordinator = new PartitionCoordinator(conf, config, context, zkState);
        lastUpdateMs = System.currentTimeMillis();
        LOG.info("====== partition size=" + coordinator.getPartitionConsumers().size());
    }


    public void close() {
        // TODO Auto-generated method stub
        zkState.close();
    }


    public void activate() {
        // TODO Auto-generated method stub

    }


    public void deactivate() {
        // TODO Auto-generated method stub

    }


    public void nextTuple() {
        Collection<PartitionConsumer> partitionConsumers = coordinator.getPartitionConsumers();
        for (PartitionConsumer consumer : partitionConsumers) {
            PartitionConsumer.EmitState state = consumer.emit(collector);
            //LOG.info("====== partition "+ consumer.getPartition() + " emit message state is "+state);
//			if(state != EmitState.EMIT_MORE) {
//				currentPartitionIndex  = (currentPartitionIndex+1) % consumerSize;
//			}
//			if(state != EmitState.EMIT_NONE) {
//				break;
//			}
        }
        long now = System.currentTimeMillis();
        if ((now - lastUpdateMs) > config.offsetUpdateIntervalMs) {
            LOG.info("commitState");
            commitState();
        }


    }

    public void commitState() {

        lastUpdateMs = System.currentTimeMillis();
        for (PartitionConsumer consumer : coordinator.getPartitionConsumers()) {
            consumer.commitState();
        }

    }


    public void ack(Object msgId) {
//		LOG.info("ack");
        KafkaMessageId messageId = (KafkaMessageId) msgId;
        PartitionConsumer consumer = coordinator.getConsumer(messageId.getPartition());
        consumer.ack(messageId.getOffset());
    }


    public void fail(Object msgId) {
        KafkaMessageId messageId = (KafkaMessageId) msgId;
        PartitionConsumer consumer = coordinator.getConsumer(messageId.getPartition());
        consumer.fail(messageId.getOffset());
        LOG.error("msg failed,partition=" + messageId.getPartition() + ",offset=" + messageId.getOffset());
    }


    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //declarer.declare(new Fields("bytes"));
        declarer.declare(new Fields("bytes", "partition"));
    }


    public Map<String, Object> getComponentConfiguration() {
        return null;
    }


}
