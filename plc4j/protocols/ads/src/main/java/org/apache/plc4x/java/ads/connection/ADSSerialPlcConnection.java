/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package org.apache.plc4x.java.ads.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import org.apache.plc4x.java.ads.api.generic.types.AMSNetId;
import org.apache.plc4x.java.ads.api.generic.types.AMSPort;
import org.apache.plc4x.java.ads.protocol.ADS2SerialProtocol;
import org.apache.plc4x.java.ads.protocol.Plc4X2ADSProtocol;
import org.apache.plc4x.java.base.connection.SerialChannelFactory;

import java.util.concurrent.CompletableFuture;

public class ADSSerialPlcConnection extends ADSAbstractPlcConnection {

    public ADSSerialPlcConnection(String serialPort, AMSNetId targetAmsNetId, AMSPort targetAmsPort) {
        this(serialPort, targetAmsNetId, targetAmsPort, generateAMSNetId(), generateAMSPort());
    }

    public ADSSerialPlcConnection(String serialPort, AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort) {
        super(new SerialChannelFactory(serialPort), targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort);
    }

    @Override
    protected ChannelHandler getChannelHandler(CompletableFuture<Void> sessionSetupCompleteFuture) {
        return new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) {
                // Build the protocol stack for communicating with the ads protocol.
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new Plc4X2ADSProtocol(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort));
                pipeline.addLast(new ADS2SerialProtocol());
            }
        };
    }

}
