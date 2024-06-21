package com.qiu.apifinal.thrift;

import lombok.Setter;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ThriftClient {
    @Setter
    private String host;

    @Setter
    private Integer port;

    private TTransport tTransport;

    private TProtocol tProtocol;

    private RecommendService.Client client;

    private void init() {
        tTransport = new TFramedTransport(new TSocket(host, port), 600);
        //协议对象 这里使用协议对象需要和服务器的一致
        tProtocol = new TCompactProtocol(tTransport);
        client = new RecommendService.Client(tProtocol);
    }

    public RecommendService.Client getService() {
        return client;
    }

    public void open() throws TTransportException {
        if (null != tTransport && !tTransport.isOpen())
            tTransport.open();
    }

    public void close() {
        if (null != tTransport && tTransport.isOpen())
            tTransport.close();
    }
}
