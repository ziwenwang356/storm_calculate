package com.tangdou.panda.kafka;


import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.ImmutableMap;
import com.tangdou.panda.utils.MetricRegistryUtils;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.Message;
import kafka.message.MessageAndOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;

import static com.codahale.metrics.MetricRegistry.name;


/**
 * @author feilaoda
 */
public class PartitionConsumer {
    private static Logger LOG = LoggerFactory.getLogger(PartitionConsumer.class);

    public static enum EmitState {
        EMIT_MORE, EMIT_END, EMIT_NONE
    }

    private int partition;
    private KafkaConsumer consumer;


    private PartitionCoordinator coordinator;

    private KafkaSpoutConfig config;
    private LinkedList<MessageAndOffset> emittingMessages = new LinkedList<MessageAndOffset>();
    private SortedSet<Long> pendingOffsets = new TreeSet<Long>();
    private SortedSet<Long> failedOffsets = new TreeSet<Long>();
    private long emittingOffset;
    private long lastCommittedOffset;
    private ZkState zkState;
    private Map stormConf;
    private Meter emitMeter;

    public PartitionConsumer(Map conf, KafkaSpoutConfig config, int partition, ZkState offsetState) {
        this.stormConf = conf;
        this.config = config;
        this.partition = partition;
        this.consumer = new KafkaConsumer(config);
        this.zkState = offsetState;
        MetricRegistry metrics = MetricRegistryUtils.getMetricRegistry();
        emitMeter = metrics.meter(name("kafka-spout", "emit", "partition-" + partition));

        Long jsonOffset = null;
        try {
            Map<Object, Object> json = offsetState.readJSON(zkPath());
            if (json != null) {
                // jsonTopologyId = (String)((Map<Object,Object>)json.get("topology"));
                jsonOffset = (Long) json.get("offset");
            }
        } catch (Throwable e) {
            LOG.warn("Error reading and/or parsing at ZkNode: " + zkPath(), e);
        }
        LOG.info("partition=" + partition + "jsonOffset=" + jsonOffset);
        try {
            if (config.fromBeginning) {
                LOG.info("fromBeginning!!!");
                emittingOffset = consumer.getOffset(config.topic, partition, kafka.api.OffsetRequest.LatestTime());
            } else {
                if (jsonOffset == null) {
                    lastCommittedOffset = consumer.getOffset(config.topic, partition, kafka.api.OffsetRequest
                            .LatestTime());
                } else {
                    lastCommittedOffset = jsonOffset;
                }
                emittingOffset = lastCommittedOffset;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public EmitState emit(SpoutOutputCollector collector) {
        if (emittingMessages.isEmpty()) {
            if (pendingOffsets.size() > 0) {
                //LOG.info("ack not received,pendingOffsets={}",pendingOffsets.size());
                return EmitState.EMIT_END;
            }
            fillMessages();
            LOG.info("fillMessages done,size={}", emittingMessages.size());
        }

        int count = 0;
        while (true) {
            MessageAndOffset toEmitMsg = emittingMessages.pollFirst();
            if (toEmitMsg == null) {
                return EmitState.EMIT_END;
            }
            count++;
            Iterable<List<Object>> tups = generateTuples(toEmitMsg.message());
            /*
            if (tups != null) {
                for (List<Object> tuple : tups) {
                    //LOG.debug("emit message {}", new String(Utils.toByteArray(toEmitMsg.message().payload())));
                    if(config.printSendInfo) {
                        try {
                            String line = new String(Utils.toByteArray(toEmitMsg.message().payload()));
                            JSONObject data = JSON.parseObject(line);
                            LOG.info(EtlBolt.isoStr2utc8Str(data.getString("@timestamp")) + "    partition=" +
                            partition);
                        } catch (Exception e) {

                        }
                    }
                    collector.emit(tuple, new KafkaMessageId(partition, toEmitMsg.offset()));
                    //collector.emit(new Values(tuple,partition), new KafkaMessageId(partition, toEmitMsg.offset()));

                }
                if(count>=config.batchSendCount) {
                    break;
                }
            } else {
                ack(toEmitMsg.offset());
            }
            */
            if (toEmitMsg.message().payload() != null) {
                String line = new String(Utils.toByteArray(toEmitMsg.message().payload()));

//                JSONObject data = JSON.parseObject(line);
//                try {
////                    LOG.info(partition+"#"+ LogDecoder.isoStr2utc8Str(data.getString("@timestamp")) +
////                            ", emittingMessages: " + emittingMessages.size() +
////                            ", pendingOffsets: " + pendingOffsets.size() +
////                            ", timestamp: " + System.currentTimeMillis());
//                } catch (ParseException e) {
//                    LOG.error("Error in EtlBolt",e);
//                }

                collector.emit(new Values(line, partition), new KafkaMessageId(partition, toEmitMsg.offset()));
                emitMeter.mark();
                if (count >= config.batchSendCount) {
                    //ack(toEmitMsg.offset());
                    break;

                }
            } else {
                ack(toEmitMsg.offset());
            }
        }

        if (emittingMessages.isEmpty()) {
            return EmitState.EMIT_END;
        } else {
            return EmitState.EMIT_MORE;
        }
    }

    private void fillMessages() {
//        LOG.info(partition + ", emittingMessages: " + emittingMessages.size() +
//                ", pendingOffsets: " + pendingOffsets.size());

        ByteBufferMessageSet msgs;
        try {
            long start = System.currentTimeMillis();
            msgs = consumer.fetchMessages(partition, emittingOffset + 1);
            //LOG.info("fetchMessages,partition {}, offset {}, msgsize {}",partition,emittingOffset,msgs.sizeInBytes());
            if (msgs == null) {
                LOG.error("fetch null message from offset {}", emittingOffset);
                emittingOffset = consumer.getOffset(config.topic, partition, kafka.api.OffsetRequest.LatestTime());
                return;
            }

            int count = 0;
            for (MessageAndOffset msg : msgs) {
                count += 1;
                emittingMessages.add(msg);
                emittingOffset = msg.offset();
//                LOG.info("pending_offsets_add_1: " + emittingOffset);
                pendingOffsets.add(emittingOffset);
                LOG.debug("fillmessage fetched a message:{}, offset:{}", msg.message().toString(), msg.offset());
            }
            long end = System.currentTimeMillis();
            //LOG.info("fetch message from partition:"+partition+", offset:" + emittingOffset+", size:"+msgs
            // .sizeInBytes()+", count:"+count +", time:"+(end-start));
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }
    }

    public void commitState() {
        LOG.info("commitState begin {}", partition);
        //LOG.info("emittingMessages size = "+emittingMessages.size());
        try {
            long lastOffset = 0;
            if (pendingOffsets.isEmpty() || pendingOffsets.size() <= 0) {
                lastOffset = emittingOffset;
            } else {
                lastOffset = pendingOffsets.first();
            }
            LOG.info("lastOffset {},lastCommittedOffset {}", lastOffset, lastCommittedOffset);

            if (lastOffset != lastCommittedOffset) {
                Map<Object, Object> data = new HashMap<Object, Object>();
                data.put("topology", stormConf.get(Config.TOPOLOGY_NAME));
                data.put("offset", lastOffset);
                data.put("partition", partition);
                data.put("broker", ImmutableMap.of("host", consumer.getLeaderBroker().host(), "port", consumer
                        .getLeaderBroker().port()));
                data.put("topic", config.topic);
                zkState.writeJSON(zkPath(), data);
                lastCommittedOffset = lastOffset;
                LOG.info("zookeeper offset update,partition=" + partition + ",offset=" + lastOffset);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

    }

    public void ack(long offset) {
//        LOG.info("ack offset: " + offset);
        try {
//            LOG.info("pending_offsets_remove_1: " + offset);
            pendingOffsets.remove(offset);
        } catch (Exception e) {
            LOG.error("offset ack error " + offset);
        }
    }

    public void fail(long offset) {
        failedOffsets.remove(offset);
//        LOG.info("pending_offsets_remove_2: " + offset);
        pendingOffsets.remove(offset);
    }

    public void close() {
        coordinator.removeConsumer(partition);
        consumer.close();
    }

    @SuppressWarnings("unchecked")
    public Iterable<List<Object>> generateTuples(Message msg) {
        Iterable<List<Object>> tups = null;
        ByteBuffer payload = msg.payload();
        if (payload == null) {
            return null;
        }
        tups = Arrays.asList(Utils.tuple(Utils.toByteArray(payload)));
        return tups;
    }

    private String zkPath() {
        //return config.zkRoot + "/kafka/offset/topic/" + config.topic + "/" + config.clientId + "/" + partition;
        return config.zkRoot + "/consumers/" + config.clientId + "/offsets/" + config.topic + "/" + partition;
    }

}
