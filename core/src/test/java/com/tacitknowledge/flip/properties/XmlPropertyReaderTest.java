/* Copyright 2012 Tacit Knowledge
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.tacitknowledge.flip.properties;

import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.model.FeatureOperation;
import com.tacitknowledge.flip.model.FeatureState;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class XmlPropertyReaderTest {
    private static final String CONFIG_FILE1 = "src/test/resources/flip-1.properties.xml";
    private static final String CONFIG_FILE = "src/test/resources/flip.properties.xml";
    
    private XmlPropertyReader propertyReader;
    private Properties props;
    
    @Before
    public void setUp() {
        propertyReader = new XmlPropertyReader();
        props = new Properties();
        System.getProperties().remove(XmlPropertyReader.CONFIG_PROPERTY);
    }
    
    @Test
    public void testFromProperties() throws IOException {
        props.setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE1);
        
        assertNotNull(propertyReader.getConfigurationStream(props));
    }
    
    @Test
    public void testFromSystemProperties() throws IOException {
        System.getProperties().setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE1);
        
        assertNotNull(propertyReader.getConfigurationStream(null));
    }
    
    @Test
    public void testFromClassPath() throws IOException {
        assertNotNull(propertyReader.getConfigurationStream(null));
    }
    
    @Test
    public void testReadWhenNoConfigFileExists() throws IOException {
        System.getProperties().setProperty(XmlPropertyReader.CONFIG_PROPERTY, "/abracadabra");
        
        propertyReader.readDescriptors();
        assertTrue(propertyReader.cache.isEmpty());
    }
    
    @Test
    public void testReadWhenNoConfigFileProperty() throws IOException {
        propertyReader.readDescriptors();
        assertTrue(propertyReader.cache.isEmpty());
    }
    
    @Test
    public void testSystemPropertiesHasPriorityOverClasspath() throws IOException {
        System.getProperties().setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(propertyReader.getConfigurationStream(null)));
        StringBuilder buf = new StringBuilder();
        String line;
        while( (line = reader.readLine()) != null ) {
            buf.append(line).append("\n");
        }
        reader.close();
        
        assertThat(buf.toString(), containsString("<feature name=\"test\""));
    }
    
    @Test
    public void testConfigPropertiesHasPriorityOverSystemProperties() throws IOException {
        props.setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE1);
        System.getProperties().setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(propertyReader.getConfigurationStream(props)));
        StringBuilder buf = new StringBuilder();
        String line;
        while( (line = reader.readLine()) != null ) {
            buf.append(line).append("\n");
        }
        reader.close();
        
        assertThat(buf.toString(), containsString("<feature name=\"test\""));
    }
    
    @Test
    public void testFallToClassPathConfig() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(propertyReader.getConfigurationStream(null)));
        StringBuilder buf = new StringBuilder();
        String line;
        while( (line = reader.readLine()) != null ) {
            buf.append(line).append("\n");
        }
        reader.close();
        
        assertThat(buf.toString(), not(containsString("<feature name=\"test\"")));
    }
    
    @Test
    public void testFindFeatureDescriptor() {
        props.setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE1);
        propertyReader.initialize(props);
        FeatureDescriptor featureDescriptor = propertyReader.getFeatureDescriptor("test");
        
        assertNotNull(featureDescriptor);
        assertEquals("test", featureDescriptor.getName());
        assertEquals(FeatureState.ENABLED, featureDescriptor.getState());
        
        assertNotNull(featureDescriptor.getRules());
        assertEquals(2, featureDescriptor.getRules().length);
        assertNotNull(featureDescriptor.getRules()[0]);
        assertEquals(FeatureState.ENABLED, featureDescriptor.getRules()[0].getState());
        assertNotNull(featureDescriptor.getRules()[0].getConditions());
        assertEquals(2, featureDescriptor.getRules()[0].getConditions().length);
        assertNotNull(featureDescriptor.getRules()[0].getConditions()[0]);
        assertEquals("a", featureDescriptor.getRules()[0].getConditions()[0].getContext());
        assertEquals("prop", featureDescriptor.getRules()[0].getConditions()[0].getName());
        assertEquals(FeatureOperation.EQUALS, featureDescriptor.getRules()[0].getConditions()[0].getOperation());
        assertEquals("1", featureDescriptor.getRules()[0].getConditions()[0].getValue());

        assertNotNull(featureDescriptor.getRules()[1]);
        assertEquals(FeatureState.DISABLED, featureDescriptor.getRules()[1].getState());
        assertNull(featureDescriptor.getRules()[1].getConditions());
    }
    
    @Test
    public void testFindFeatureWithJexlConditionDescriptor() {
        props.setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE1);
        propertyReader.initialize(props);
        FeatureDescriptor featureDescriptor = propertyReader.getFeatureDescriptor("test");
        
        assertNotNull(featureDescriptor);
        assertEquals("test", featureDescriptor.getName());
        assertEquals(FeatureState.ENABLED, featureDescriptor.getState());
        
        assertNotNull(featureDescriptor.getRules());
        assertEquals(2, featureDescriptor.getRules().length);
        assertNotNull(featureDescriptor.getRules()[0]);
        assertEquals(FeatureState.ENABLED, featureDescriptor.getRules()[0].getState());
        assertNotNull(featureDescriptor.getRules()[0].getConditions());
        assertEquals(2, featureDescriptor.getRules()[0].getConditions().length);
        assertNotNull(featureDescriptor.getRules()[0].getConditions()[0]);
        assertEquals("a = 1", featureDescriptor.getRules()[0].getConditions()[1].getExpression());
        assertEquals("a", featureDescriptor.getRules()[0].getConditions()[0].getContext());
        assertEquals("prop", featureDescriptor.getRules()[0].getConditions()[0].getName());
        assertEquals(FeatureOperation.EQUALS, featureDescriptor.getRules()[0].getConditions()[0].getOperation());
        assertEquals("1", featureDescriptor.getRules()[0].getConditions()[0].getValue());

        assertNotNull(featureDescriptor.getRules()[1]);
        assertEquals(FeatureState.DISABLED, featureDescriptor.getRules()[1].getState());
        assertNull(featureDescriptor.getRules()[1].getConditions());
    }

    @Test
    public void testFindInvalidFeatureDescriptor() {
        props.setProperty(XmlPropertyReader.CONFIG_PROPERTY, CONFIG_FILE1);
        propertyReader.initialize(props);
        FeatureDescriptor featureDescriptor = propertyReader.getFeatureDescriptor("test1");
        
        assertNull(featureDescriptor);
    }
    
    @Test
    public void testRereadFile() throws IOException, InterruptedException {
        File in1 = new File(CONFIG_FILE);
        File in2 = new File(CONFIG_FILE1);
        File out = File.createTempFile("flip_", ".xml");
        copyFile(in1, out);
        
        props.setProperty(XmlPropertyReader.CONFIG_PROPERTY, out.getAbsolutePath());
        props.setProperty(XmlPropertyReader.CONFIG_REREAD_INTERVAL, "1");
        propertyReader.initialize(props);
        
        assertNull(propertyReader.getFeatureDescriptor("test"));
        copyFile(in2, out);
        synchronized(this) {
            this.wait(2000);
        }
        assertNotNull(propertyReader.getFeatureDescriptor("test"));
    }
    
    private void copyFile(File src, File dst) {
        try {
            dst.delete();
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);
            
            byte[] buf = new byte[1024];
            int count;
            while( (count = in.read(buf)) >= 0 ) {
                out.write(buf, 0, count);
            }
            
            in.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(XmlPropertyReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
