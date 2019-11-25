package com.gcg.nifi.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

@SideEffectFree
@Tags({"json replace index", "replace"})
@CapabilityDescription("Replacer According to Index.")
public class ParseJsonAttribute extends AbstractProcessor {

	private static ComponentLog log;
	
	private Set<Relationship> relationships;
	private List<PropertyDescriptor> properties;
	
	public static final PropertyDescriptor JSONATTRIBUTE = new PropertyDescriptor.Builder()
    .name("Json Atrribute")
    .required(true)
    .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    .build();
	
	public static final PropertyDescriptor JSONPROPERTIES = new PropertyDescriptor.Builder()
    .name("Json Properties")
    .required(true)
    .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    .build();
	
	
	public static final Relationship SUCCESS = new Relationship.Builder()
    .name("success")
    .description("Succes relationship")
    .build();

	public static final Relationship FAIL = new Relationship.Builder()
	.name("fail")
	.description("Fail relationship. If JSON parse throw exception.")
	.build();
	
	
	@Override
	public void init(final ProcessorInitializationContext context){
		
		log = getLogger();
		
		List<PropertyDescriptor> properties = new ArrayList<>();
	    properties.add(JSONATTRIBUTE);
	    properties.add(JSONPROPERTIES);
	    this.properties = Collections.unmodifiableList(properties);
		
	    Set<Relationship> relationships = new HashSet<>();
	    relationships.add(SUCCESS);
	    relationships.add(FAIL);
	    this.relationships = Collections.unmodifiableSet(relationships);
	    
	    log.info("-------------Init OK-------------");
	}
	
	@Override
	public void onTrigger(ProcessContext context, ProcessSession session)
			throws ProcessException {
		
		FlowFile flowfile = session.get();
		
		String attribute = flowfile.getAttribute(context.getProperty(JSONATTRIBUTE).getValue());
		String properties = context.getProperty(JSONPROPERTIES).getValue();
		String[] propertyArray = properties.split(",");
		
		try{
		
			for (String property : propertyArray){
				
				String propertyTrim = property.trim();
				
				JSONObject obj = (JSONObject)JSONValue.parseWithException(attribute);
				
				log.info(obj.getAsString(propertyTrim));
				flowfile = session.putAttribute(flowfile, propertyTrim, obj.getAsString(propertyTrim));
			}
			
			session.transfer(flowfile, SUCCESS);
		
		}catch(ParseException ex){
			session.transfer(flowfile, FAIL);
		}

	}
	
	/*
	 *
	 * This props is for configuration.
	 * 
	 */
	@Override
	public Set<Relationship> getRelationships(){
		return relationships;
	}
	
	@Override
	 public List<PropertyDescriptor> getSupportedPropertyDescriptors(){
       return properties;
	 }
	
}
