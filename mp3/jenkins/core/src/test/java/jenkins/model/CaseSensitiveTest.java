package jenkins.model;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import hudson.EnvVars;
import hudson.model.Computer;
import hudson.model.Job;
import hudson.model.TaskListener;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.Issue;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

public class CaseSensitiveTest {
    IdStrategy.CaseSensitive instance;

    @Before
    public void setUp() throws Exception {
        instance = new IdStrategy.CaseSensitive();
    }

    @Test
    public void testEasy(){
        assertEquals("abc0", instance.idFromFilename("abc0"));
    }

    @Test
    public void testChanges(){
        assertEquals("abcab", instance.idFromFilename("abc$0061b"));
    }
}
