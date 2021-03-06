/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights
 *                             reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.sdnc.vnfapi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.provider.SvcLogicService;
import org.opendaylight.yang.gen.v1.org.onap.sdnctl.vnf.rev150720.preload.data.PreloadDataBuilder;
import org.opendaylight.yang.gen.v1.org.onap.sdnctl.vnf.rev150720.service.data.ServiceDataBuilder;
import org.opendaylight.yang.gen.v1.org.onap.sdnctl.vnf.rev150720.vf.module.preload.data.VfModulePreloadDataBuilder;
import org.opendaylight.yang.gen.v1.org.onap.sdnctl.vnf.rev150720.vf.module.service.data.VfModuleServiceDataBuilder;
import org.opendaylight.yang.gen.v1.org.onap.sdnctl.vnf.rev150720.vnf.instance.preload.data.VnfInstancePreloadDataBuilder;
import org.opendaylight.yang.gen.v1.org.onap.sdnctl.vnf.rev150720.vnf.instance.service.data.VnfInstanceServiceDataBuilder;
import org.slf4j.Logger;

import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VNFSDNSvcLogicServiceClientTest {
    private static final String MODE = "mode";
    private static final String MODULE = "module";
    private static final String RPC = "rpc";
    private static final String VERSION = "version";

    private Logger mockLog = mock(Logger.class);
    private SvcLogicService mockSvcLogic = mock(SvcLogicService.class);

    private VNFSDNSvcLogicServiceClient svcClient;

    @Before
    public void setUp() throws Exception {
        svcClient = spy(new VNFSDNSvcLogicServiceClient(mockSvcLogic));

        Whitebox.setInternalState(svcClient, "logger", mockLog);
        Whitebox.setInternalState(svcClient, "svcLogic", mockSvcLogic);
    }

    @Test
    public void testConstructorWithoutSvcLogicBundle() throws Exception {
        VNFSDNSvcLogicServiceClient client = new VNFSDNSvcLogicServiceClient(mockSvcLogic);
        Assert.assertEquals("Should have set mockSvcLogic",
                mockSvcLogic, Whitebox.getInternalState(client, "svcLogic"));
    }

    @Test (expected = SvcLogicException.class)
    public void testHasGraphWithException() throws Exception {
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).hasGraph(MODULE, RPC, VERSION, MODE);
        svcClient.hasGraph(MODULE, RPC, VERSION, MODE);
    }

    @Test
    public void testHasGraph() throws Exception {
        Mockito.doReturn(true).when(mockSvcLogic).hasGraph(MODULE, RPC, VERSION, MODE);
        Assert.assertTrue("Should return true", svcClient.hasGraph(MODULE, RPC, VERSION, MODE));
    }

    // --------- Test cases for ServiceDataBuilder serviceData-----------------
    @Test (expected = SvcLogicException.class)
    public void testExecuteWithServiceDataBuilderWithException() throws Exception {
        ServiceDataBuilder mockBuilder = mock(ServiceDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
    }

    @Test
    public void testExecuteWithServiceDataBuilder() throws Exception {
        ServiceDataBuilder mockBuilder = mock(ServiceDataBuilder.class);
        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
        Mockito.verify(svcClient, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class),
                any(ServiceDataBuilder.class), any(Properties.class));
    }

    @Test (expected = SvcLogicException.class)
    public void testParamExecuteWithServiceDataBuilderWithException() throws Exception {
        ServiceDataBuilder mockBuilder = mock(ServiceDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
    }

    @Test
    public void testParamExecuteWithServiceDataBuilderWithExecutorReturnNull() throws Exception {
        ServiceDataBuilder mockBuilder = mock(ServiceDataBuilder.class);
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertTrue("Should return null", properties == null);
        Mockito.verify(mockSvcLogic, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
    }

    @Test
    public void testParamExecuteWithServiceDataBuilderWithExecutorReturnFailure() throws Exception {
        ServiceDataBuilder mockBuilder = mock(ServiceDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty(VNFSDNSvcLogicServiceClient.SVC_LOGIC_STATUS_KEY,
                VNFSDNSvcLogicServiceClient.FAILURE_RESULT);
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should return resultProps", resultProps, properties);
    }

    @Test
    public void testParamExecuteWithServiceDataBuilder() throws Exception {
        Mockito.doReturn(true).when(mockLog).isDebugEnabled();
        ServiceDataBuilder mockBuilder = mock(ServiceDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty("my", "testing");
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should still return resultProps", resultProps, properties);
    }

    // --------- Test cases for PreloadDataBuilder serviceData-----------------
    @Test (expected = SvcLogicException.class)
    public void testExecuteWithPreloadDataBuilderWithException() throws Exception {
        PreloadDataBuilder mockBuilder = mock(PreloadDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
    }

    @Test
    public void testExecuteWithPreloadDataBuilder() throws Exception {
        PreloadDataBuilder  mockBuilder = mock(PreloadDataBuilder.class);
        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
        Mockito.verify(svcClient, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class),
                any(PreloadDataBuilder .class), any(Properties.class));
    }

    @Test (expected = SvcLogicException.class)
    public void testParamExecuteWithPreloadDataBuilderWithException() throws Exception {
        PreloadDataBuilder mockBuilder = mock(PreloadDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
    }

    @Test
    public void testParamExecuteWithPreloadDataBuilderWithExecutorReturnNull() throws Exception {
        PreloadDataBuilder mockBuilder = mock(PreloadDataBuilder.class);
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertTrue("Should return null", properties == null);
        Mockito.verify(mockSvcLogic, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
    }

    @Test
    public void testParamExecuteWithPreloadDataBuilderWithExecutorReturnFailure() throws Exception {
        PreloadDataBuilder mockBuilder = mock(PreloadDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty(VNFSDNSvcLogicServiceClient.SVC_LOGIC_STATUS_KEY,
                VNFSDNSvcLogicServiceClient.FAILURE_RESULT);
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should return resultProps", resultProps, properties);
    }

    @Test
    public void testParamExecuteWithPreloadDataBuilder() throws Exception {
        Mockito.doReturn(true).when(mockLog).isDebugEnabled();
        PreloadDataBuilder mockBuilder = mock(PreloadDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty("my", "testing");
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should still return resultProps", resultProps, properties);
    }


    // --------- Test cases for VfModulePreloadDataBuilder serviceData-----------------
    @Test (expected = SvcLogicException.class)
    public void testExecuteWithVfModulePreloadDataBuilderWithException() throws Exception {
        VfModulePreloadDataBuilder mockBuilder = mock(VfModulePreloadDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
    }

    @Test
    public void testExecuteWithVfModulePreloadDataBuilder() throws Exception {
        VfModulePreloadDataBuilder  mockBuilder = mock(VfModulePreloadDataBuilder.class);
        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
        Mockito.verify(svcClient, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class),
                any(VfModulePreloadDataBuilder .class), any(Properties.class));
    }

    @Test (expected = SvcLogicException.class)
    public void testParamExecuteWithVfModulePreloadDataBuilderWithException() throws Exception {
        VfModulePreloadDataBuilder mockBuilder = mock(VfModulePreloadDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
    }

    @Test
    public void testParamExecuteWithVfModulePreloadDataBuilderWithExecutorReturnNull() throws Exception {
        VfModulePreloadDataBuilder mockBuilder = mock(VfModulePreloadDataBuilder.class);
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertTrue("Should return null", properties == null);
        Mockito.verify(mockSvcLogic, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
    }

    @Test
    public void testParamExecuteWithVfModulePreloadDataBuilderWithExecutorReturnFailure() throws Exception {
        VfModulePreloadDataBuilder mockBuilder = mock(VfModulePreloadDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty(VNFSDNSvcLogicServiceClient.SVC_LOGIC_STATUS_KEY,
                VNFSDNSvcLogicServiceClient.FAILURE_RESULT);
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should return resultProps", resultProps, properties);
    }

    @Test
    public void testParamExecuteWithVfModulePreloadDataBuilder() throws Exception {
        Mockito.doReturn(true).when(mockLog).isDebugEnabled();
        VfModulePreloadDataBuilder mockBuilder = mock(VfModulePreloadDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty("my", "testing");
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should still return resultProps", resultProps, properties);
    }

    // --------- Test cases for VfModuleServiceDataBuilder serviceData-----------------
    @Test (expected = SvcLogicException.class)
    public void testExecuteWithVfModuleServiceDataBuilderWithException() throws Exception {
        VfModuleServiceDataBuilder mockBuilder = mock(VfModuleServiceDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
    }

    @Test
    public void testExecuteWithVfModuleServiceDataBuilder() throws Exception {
        VfModuleServiceDataBuilder  mockBuilder = mock(VfModuleServiceDataBuilder.class);
        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
        Mockito.verify(svcClient, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class),
                any(VfModuleServiceDataBuilder .class), any(Properties.class));
    }

    @Test (expected = SvcLogicException.class)
    public void testParamExecuteWithVfModuleServiceDataBuilderWithException() throws Exception {
        VfModuleServiceDataBuilder mockBuilder = mock(VfModuleServiceDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
    }

    @Test
    public void testParamExecuteWithVfModuleServiceDataBuilderWithExecutorReturnNull() throws Exception {
        VfModuleServiceDataBuilder mockBuilder = mock(VfModuleServiceDataBuilder.class);
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertTrue("Should return null", properties == null);
        Mockito.verify(mockSvcLogic, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
    }

    @Test
    public void testParamExecuteWithVfModuleServiceDataBuilderWithExecutorReturnFailure() throws Exception {
        VfModuleServiceDataBuilder mockBuilder = mock(VfModuleServiceDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty(VNFSDNSvcLogicServiceClient.SVC_LOGIC_STATUS_KEY,
                VNFSDNSvcLogicServiceClient.FAILURE_RESULT);
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should return resultProps", resultProps, properties);
    }

    @Test
    public void testParamExecuteWithVfModuleServiceDataBuilder() throws Exception {
        Mockito.doReturn(true).when(mockLog).isDebugEnabled();
        VfModuleServiceDataBuilder mockBuilder = mock(VfModuleServiceDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty("my", "testing");
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should still return resultProps", resultProps, properties);
    }

    // --------- Test cases for VnfInstancePreloadDataBuilder serviceData-----------------
    @Test (expected = SvcLogicException.class)
    public void testExecuteWithVnfInstancePreloadDataBuilderWithException() throws Exception {
        VnfInstancePreloadDataBuilder mockBuilder = mock(VnfInstancePreloadDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
    }

    @Test
    public void testExecuteWithVnfInstancePreloadDataBuilder() throws Exception {
        VnfInstancePreloadDataBuilder  mockBuilder = mock(VnfInstancePreloadDataBuilder.class);
        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
        Mockito.verify(svcClient, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class),
                any(VnfInstancePreloadDataBuilder .class), any(Properties.class));
    }

    @Test (expected = SvcLogicException.class)
    public void testParamExecuteWithVnfInstancePreloadDataBuilderWithException() throws Exception {
        VnfInstancePreloadDataBuilder mockBuilder = mock(VnfInstancePreloadDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
    }

    @Test
    public void testParamExecuteWithVnfInstancePreloadDataBuilderWithExecutorReturnNull() throws Exception {
        VnfInstancePreloadDataBuilder mockBuilder = mock(VnfInstancePreloadDataBuilder.class);
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertTrue("Should return null", properties == null);
        Mockito.verify(mockSvcLogic, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
    }

    @Test
    public void testParamExecuteWithVnfInstancePreloadDataBuilderWithExecutorReturnFailure() throws Exception {
        VnfInstancePreloadDataBuilder mockBuilder = mock(VnfInstancePreloadDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty(VNFSDNSvcLogicServiceClient.SVC_LOGIC_STATUS_KEY,
                VNFSDNSvcLogicServiceClient.FAILURE_RESULT);
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should return resultProps", resultProps, properties);
    }

    @Test
    public void testParamExecuteWithVnfInstancePreloadDataBuilder() throws Exception {
        Mockito.doReturn(true).when(mockLog).isDebugEnabled();
        VnfInstancePreloadDataBuilder mockBuilder = mock(VnfInstancePreloadDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty("my", "testing");
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should still return resultProps", resultProps, properties);
    }

    // --------- Test cases for VnfInstanceServiceDataBuilder serviceData-----------------
    @Test (expected = SvcLogicException.class)
    public void testExecuteWithVnfInstanceServiceDataBuilderWithException() throws Exception {
        VnfInstanceServiceDataBuilder mockBuilder = mock(VnfInstanceServiceDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
    }

    @Test
    public void testExecuteWithVnfInstanceServiceDataBuilder() throws Exception {
        VnfInstanceServiceDataBuilder  mockBuilder = mock(VnfInstanceServiceDataBuilder.class);
        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder);
        Mockito.verify(svcClient, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class),
                any(VnfInstanceServiceDataBuilder .class), any(Properties.class));
    }

    @Test (expected = SvcLogicException.class)
    public void testParamExecuteWithVnfInstanceServiceDataBuilderWithException() throws Exception {
        VnfInstanceServiceDataBuilder mockBuilder = mock(VnfInstanceServiceDataBuilder.class);
        Mockito.doThrow(new SvcLogicException()).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));

        svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
    }

    @Test
    public void testParamExecuteWithVnfInstanceServiceDataBuilderWithExecutorReturnNull() throws Exception {
        VnfInstanceServiceDataBuilder mockBuilder = mock(VnfInstanceServiceDataBuilder.class);
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertTrue("Should return null", properties == null);
        Mockito.verify(mockSvcLogic, times(1)).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
    }

    @Test
    public void testParamExecuteWithVnfInstanceServiceDataBuilderWithExecutorReturnFailure() throws Exception {
        VnfInstanceServiceDataBuilder mockBuilder = mock(VnfInstanceServiceDataBuilder.class);
        Properties resultProps = new Properties();
        resultProps.setProperty(VNFSDNSvcLogicServiceClient.SVC_LOGIC_STATUS_KEY,
                VNFSDNSvcLogicServiceClient.FAILURE_RESULT);
        Mockito.doReturn(resultProps).when(mockSvcLogic).execute(
                any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class));
        Properties properties = svcClient.execute(MODULE, RPC, VERSION, MODE, mockBuilder, new Properties());
        Assert.assertEquals("Should return resultProps", resultProps, properties);
    }

}