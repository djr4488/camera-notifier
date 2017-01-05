package org.djr.securus.camera;

import junit.framework.TestCase;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 1/4/17.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class})
public class CameraHttpTriggerServiceTest extends TestCase {
    @Produces
    @Mock
    private Client client;
    @Mock
    private Invocation.Builder builder;
    @Inject
    private CameraHttpTriggerService cameraHttpTriggerService;
    private static final int NON_200_RESPONSE = 100;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoCameraTriggerSuccess() {
        String base64Auth = cameraHttpTriggerService.getAuthorizationToken("test", "test");
        WebTarget webTarget = mock(WebTarget.class);
        when(client.target("http://192.168.1.123/camera/trigger")).thenReturn(webTarget);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(webTarget.request(MediaType.TEXT_HTML_TYPE)).thenReturn(builder);
        when(builder.header("Authorization", "Basic " + base64Auth)).thenReturn(builder);
        Response mockedResponse = mock(Response.class);
        when(builder.get()).thenReturn(mockedResponse);
        when(mockedResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        assertTrue(cameraHttpTriggerService.doCameraTrigger("test", "test", "http://192.168.1.123/camera/trigger"));
    }

    @Test
    public void testDoCameraTriggerNonSuccess() {
        String base64Auth = cameraHttpTriggerService.getAuthorizationToken("test", "test");
        WebTarget webTarget = mock(WebTarget.class);
        when(client.target("http://192.168.1.123/camera/trigger")).thenReturn(webTarget);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(webTarget.request(MediaType.TEXT_HTML_TYPE)).thenReturn(builder);
        when(builder.header("Authorization", "Basic " + base64Auth)).thenReturn(builder);
        Response mockedResponse = mock(Response.class);
        when(builder.get()).thenReturn(mockedResponse);
        when(mockedResponse.getStatus()).thenReturn(Response.Status.UNAUTHORIZED.getStatusCode());
        assertFalse(cameraHttpTriggerService.doCameraTrigger("test", "test", "http://192.168.1.123/camera/trigger"));
    }

    @Test
    public void testDoCameraTriggerNonSuccessLessThan200Status() {
        String base64Auth = cameraHttpTriggerService.getAuthorizationToken("test", "test");
        WebTarget webTarget = mock(WebTarget.class);
        when(client.target("http://192.168.1.123/camera/trigger")).thenReturn(webTarget);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(webTarget.request(MediaType.TEXT_HTML_TYPE)).thenReturn(builder);
        when(builder.header("Authorization", "Basic " + base64Auth)).thenReturn(builder);
        Response mockedResponse = mock(Response.class);
        when(builder.get()).thenReturn(mockedResponse);
        when(mockedResponse.getStatus()).thenReturn(NON_200_RESPONSE);
        assertFalse(cameraHttpTriggerService.doCameraTrigger("test", "test", "http://192.168.1.123/camera/trigger"));
    }
}
