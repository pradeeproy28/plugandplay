package com.tech.plugandplay.api;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.annotations.providers.jackson.Formatted;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tech.plugandplay.hibernate.HibernateUtil;
import com.tech.plugandplay.model.Business;
import com.tech.plugandplay.model.Top100;
import com.tech.plugandplay.model.Top100List;
import com.tech.plugandplay.model.Top20;
import com.tech.plugandplay.model.Top20List;
import com.tech.plugandplay.model.Ventures;
import com.tech.plugandplay.util.Constants;

@Path("/v1")
public class RestService {
	
	private static Log log = LogFactory.getLog(RestService.class);
	
	@GET
    @Path("/ventures/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getAllVentures() {
		
    	List<Ventures> ventures = HibernateUtil.getAllVentures();
       	if(!ventures.isEmpty()){
    		return Response.ok(ventures).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any companies.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/ventures/query/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getVenturesPage(@PathParam("page") int page, @QueryParam("query") String query) {
		
		JSONObject pagination = new JSONObject();
		List<Ventures> ventures = null;
		if(query.length() > 2 && query.length() < 32 ){
			pagination.put("count", HibernateUtil.getVenturesSearchCount(query));
	    	ventures = HibernateUtil.getVenturesPage(page, query);
	    	pagination.put("data", ventures);
		} else {
			pagination.put("count", HibernateUtil.getVenturesCount());
	    	ventures = HibernateUtil.getVenturesPage(page);
	    	pagination.put("data", ventures);
		}
       	if(!ventures.isEmpty()){
    		return Response.ok(pagination.toString()).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any content.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@POST
    @Path("/lucene")
    public Response updateLucene() {
		HibernateUtil.updateLuceneIndex();
		return Response.ok().build();
		
	}
	
	@GET
    @Path("/top100/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getTop100(@QueryParam("listName") String listName) {
		
    	List<Top100> top100 = HibernateUtil.getAllTop100(listName);
       	if(!top100.isEmpty()){
       		StringBuilder ids = new StringBuilder();
       		for(Top100 venture: top100){
       			ids.append(venture.getVenture_id());
       			ids.append(",");
       		} 		
       		List<Ventures> ventures = HibernateUtil.getVentureTop100(ids.toString().substring(0, ids.toString().length()-1));
       		return Response.ok(ventures).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any top 100.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/top20/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getTop20(@QueryParam("listName") String listName) {
		
    	List<Top20> top20 = HibernateUtil.getAllTop20(listName);
       	if(!top20.isEmpty()){
       		StringBuilder ids = new StringBuilder();
       		for(Top20 venture: top20){
       			ids.append(venture.getVenture_id());
       			ids.append(",");
       		} 		
       		List<Ventures> ventures = HibernateUtil.getVentureTop20(ids.toString().substring(0, ids.toString().length()-1));
       		return Response.ok(ventures).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any top 20.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/top100/lists")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getTop100Lists() {
	
    	List<Top100List> top100list = HibernateUtil.getTop100Lists();
       	if(!top100list.isEmpty()){
       		return Response.ok(top100list).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any top 100 lists.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/top20/lists")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getTop20Lists() {

    	List<Top20List> top20list = HibernateUtil.getTop20Lists();
       	if(!top20list.isEmpty()){
       		return Response.ok(top20list).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any top 20 lists.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/top100/archived")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getTop100ListsArchived() {

    	List<Top100List> top100list = HibernateUtil.getTop100ListsArchived();
       	if(!top100list.isEmpty()){
       		return Response.ok(top100list).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any archived top 100 lists.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/top20/archived")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getTop20ListsArchived() {

    	List<Top20List> top20list = HibernateUtil.getTop20ListsArchived();
       	if(!top20list.isEmpty()){
       		return Response.ok(top20list).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any archived top 20 lists.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@DELETE
	@Path("/top100/delete/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	@Formatted
    public Response deleteFromTop100(@PathParam("id") int id, @QueryParam("listName") String listName){
		//log.info("Delete from top 100 path param:"+ id);
		Top100 deleteMe = HibernateUtil.getTop100(id, listName);
		if(deleteMe == null){
			return Response.status(Constants.HTTPCodes.BAD_REQUEST).entity("Could not delete top 100!").header("Access-Control-Allow-Origin", "*").build();
		}
		List<Top100> top100list = HibernateUtil.getAllTop100(listName);
		for(int i = deleteMe.getOrder(); i < top100list.size(); i++){
	    	Top100 tmp = top100list.get(i);
	    	tmp.setOrder(tmp.getOrder()-1);
	    	top100list.set(i-1, tmp);
	    }
		top100list.remove(top100list.size()-1);
		Ventures venture = HibernateUtil.getVenture(id);
		venture.removeTop100(deleteMe);
		HibernateUtil.updateVenture(venture);
	    for(Top100 list : top100list){
	    	HibernateUtil.updateTop100(list);
	    }
		return Response.ok(deleteMe).header("Access-Control-Allow-Origin", "*").build();
		
	}

	@DELETE
	@Path("/top100/delete/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	@Formatted
    public Response deleteFromTop20(@PathParam("id") int id, @QueryParam("listName") String listName){
		//log.info("Delete from top 20 path param:"+ id);
		Top20 deleteMe = HibernateUtil.getTop20(id, listName);
		if(deleteMe == null){
			return Response.status(Constants.HTTPCodes.BAD_REQUEST).entity("Could not delete top 20!").header("Access-Control-Allow-Origin", "*").build();
		}
		List<Top20> top20list = HibernateUtil.getAllTop20(listName);
		for(int i = deleteMe.getOrder(); i < top20list.size(); i++){
	    	Top20 tmp = top20list.get(i);
	    	tmp.setOrder(tmp.getOrder()-1);
	    	top20list.set(i-1, tmp);
	    }
		top20list.remove(top20list.size()-1);
		Ventures venture = HibernateUtil.getVenture(id);
		venture.removeTop20(deleteMe);
		HibernateUtil.updateVenture(venture);
	    for(Top20 list : top20list){
	    	HibernateUtil.updateTop20(list);
	    }
		return Response.ok(deleteMe).header("Access-Control-Allow-Origin", "*").build();
		
	}
	
	@POST
    @Path("/top100/move")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response moveTop100(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		//log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);
		
		Top100 current = HibernateUtil.getTop100(body.getInt("id"), body.getString("listName"));
		int order = body.getInt("order");
		List<Top100> top100list = HibernateUtil.getAllTop100(body.getString("listName"));
		if(order < 1 || order > 100 || order == current.getOrder() || order > top100list.size()){
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString("The order value "+order+" is not valid! Please try again.");
			//log.info(jsonInString);
			return Response.status(Constants.HTTPCodes.BAD_REQUEST).entity(jsonInString).header("Access-Control-Allow-Origin", "*").build();	
		}
		
	    for(int i = current.getOrder(); i < top100list.size(); i++){
	    	Top100 tmp = top100list.get(i);
	    	tmp.setOrder(tmp.getOrder()-1);
	    	top100list.set(i-1, tmp);
	    }
	    top100list.remove(top100list.size()-1);
	    for(int i = order - 1 ; i < top100list.size(); i++){
	    	Top100 tmp = top100list.get(i);
	    	tmp.setOrder(tmp.getOrder()+1);
	    	top100list.set(i, tmp);
	    } 
	    current.setOrder(order);
	    top100list.add(order-1, current);
	    for(Top100 list : top100list){
	    	HibernateUtil.updateTop100(list);
	    }
		
		return Response.ok(current).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/top20/move")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response moveTop20(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		//log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);
		
		Top20 current = HibernateUtil.getTop20(body.getInt("id"), body.getString("listName"));
		int order = body.getInt("order");
		List<Top20> top20list = HibernateUtil.getAllTop20(body.getString("listName"));
		if(order < 1 || order > 100 || order == current.getOrder() || order > top20list.size()){
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString("The order value "+order+" is not valid! Please try again.");
			//log.info(jsonInString);
			return Response.status(Constants.HTTPCodes.BAD_REQUEST).entity(jsonInString).header("Access-Control-Allow-Origin", "*").build();	
		}
		
	    for(int i = current.getOrder(); i < top20list.size(); i++){
	    	Top20 tmp = top20list.get(i);
	    	tmp.setOrder(tmp.getOrder()-1);
	    	top20list.set(i-1, tmp);
	    }
	    top20list.remove(top20list.size()-1);
	    for(int i = order - 1 ; i < top20list.size(); i++){
	    	Top20 tmp = top20list.get(i);
	    	tmp.setOrder(tmp.getOrder()+1);
	    	top20list.set(i, tmp);
	    } 
	    current.setOrder(order);
	    top20list.add(order-1, current);
	    for(Top20 list : top20list){
	    	HibernateUtil.updateTop20(list);
	    }
		
		return Response.ok(current).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
    @Path("/ventures/portfolio")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getPortfolioCompanies() {
		
    	List<Ventures> ventures = HibernateUtil.getPortfolioCompanies();
       	if(!ventures.isEmpty()){
    		return Response.ok(ventures).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any companies.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/ventures/{id}")
    @Produces("application/json")
	@Formatted
    public Response getVentureById(@PathParam("id") int id){
		
		Ventures venture = HibernateUtil.getVenture(id);
    	System.out.println("Company Info");
    	if(venture != null){
    		return Response.ok(venture).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Entity not found for ID: " + id).header("Access-Control-Allow-Origin", "*").build();
    	}
		
	}
	
	@POST
    @Path("/ventures/addtop100")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response addTop100(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);
		List<Top100> top100list = HibernateUtil.getAllTop100(body.getString("listName"));
		if(top100list.size()==100){
			return Response.status(Status.PARTIAL_CONTENT).entity(Constants.GenericErrorMessages.EXCEEDED_SIZE).header("Access-Control-Allow-Origin", "*").build();
		}
		//Check to see if part of top100 already
		if(HibernateUtil.getTop100(body.getInt("id"), body.getString("listName")) != null){
			return Response.status(Status.NO_CONTENT).entity("Top 100 venture with id "+body.getInt("id")+" already exists in list "+body.getString("listName")+"!").header("Access-Control-Allow-Origin", "*").build();
		}
		Top100 top100 = new Top100();
		top100.setOrder(top100list.size()+1);
		top100.setVenture_id(body.getInt("id"));
		top100.setListName(body.getString("listName"));
		HibernateUtil.addTop100(top100);
		Ventures venture = HibernateUtil.getVenture(body.getInt("id"));
		venture.addTop100(top100);
		HibernateUtil.updateVenture(venture);
		return Response.ok(top100).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/ventures/addtop20")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response addTop20(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		//log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);
		List<Top20> top20list = HibernateUtil.getAllTop20(body.getString("listName"));
		if(top20list.size()==20){
			return Response.status(Status.PARTIAL_CONTENT).entity(Constants.GenericErrorMessages.EXCEEDED_SIZE).header("Access-Control-Allow-Origin", "*").build();
		}
		//Check to see if part of top100 already
		if(HibernateUtil.getTop20(body.getInt("id"), body.getString("listName")) != null){
			return Response.status(Status.NO_CONTENT).entity("Top 20 venture with id "+body.getInt("id")+" already exists in list "+body.getString("listName")+"!").header("Access-Control-Allow-Origin", "*").build();
		}
		Top20 top20 = new Top20();
		top20.setOrder(top20list.size()+1);
		top20.setVenture_id(body.getInt("id"));
		top20.setListName(body.getString("listName"));
		HibernateUtil.addTop20(top20);
		Ventures venture = HibernateUtil.getVenture(body.getInt("id"));
		venture.addTop20(top20);
		HibernateUtil.updateVenture(venture);
		return Response.ok(top20).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/top100/newlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response newTop100List(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		//log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);

		//Check to see if part of top100 already
		if(HibernateUtil.getTop100ListByName(body.getString("listName")) != null){
			ObjectMapper mapper = new ObjectMapper();
			return Response.status(Constants.HTTPCodes.BAD_REQUEST).entity(mapper.writeValueAsString("Top 100 list with id "+body.getString("listName")+" already exists!")).header("Access-Control-Allow-Origin", "*").build();
		}
		Top100List top100list = new Top100List();
		top100list.setListName(body.getString("listName"));
		top100list.setArchive(new Boolean(false));
		top100list.setTime(new Date());
		top100list = HibernateUtil.addTop100List(top100list);
		//List<Top100List> newlist = HibernateUtil.getTop100Lists();
		return Response.ok(top100list).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/top20/newlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response newTop20List(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		//log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);

		//Check to see if part of top20 already
		if(HibernateUtil.getTop20ListByName(body.getString("listName")) != null){
			ObjectMapper mapper = new ObjectMapper();
			return Response.status(Constants.HTTPCodes.BAD_REQUEST).entity(mapper.writeValueAsString("Top 20 list with id "+body.getString("listName")+" already exists!")).header("Access-Control-Allow-Origin", "*").build();
		}
		Top20List top20list = new Top20List();
		top20list.setListName(body.getString("listName"));
		top20list.setArchive(new Boolean(false));
		top20list.setTime(new Date());
		top20list = HibernateUtil.addTop20List(top20list);

		return Response.ok(top20list).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/top100/archivelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response archiveTop100List(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		//log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);

		Top100List top100list = HibernateUtil.getTop100List(body.getInt("id"));

		top100list.setArchive(new Boolean(true));
		HibernateUtil.updateTop100List(top100list);

		return Response.ok(top100list).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/top20/archivelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response archiveTop20List(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		//log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);

		Top20List top20list = HibernateUtil.getTop20List(body.getInt("id"));

		top20list.setArchive(new Boolean(true));
		HibernateUtil.updateTop20List(top20list);

		return Response.ok(top20list).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/top100/unarchivelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response unarchiveTop100List(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);

		Top100List top100list = HibernateUtil.getTop100List(body.getInt("id"));

		top100list.setArchive(new Boolean(false));
		HibernateUtil.updateTop100List(top100list);

		return Response.ok(top100list).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/top20/unarchivelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response unarchiveTop20List(String json) throws JsonGenerationException, JsonMappingException, IOException {
		
		log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);

		Top20List top20list = HibernateUtil.getTop20List(body.getInt("id"));

		top20list.setArchive(new Boolean(false));
		HibernateUtil.updateTop20List(top20list);

		return Response.ok(top20list).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
    @Path("/ventures/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response newVenture(String json) {
		
		log.info("JSON body:"+ json);
		JSONObject body = new JSONObject(json);
		JSONArray fields = body.getJSONObject("form_response").getJSONObject("definition").getJSONArray("fields");
		JSONArray answers = body.getJSONObject("form_response").getJSONArray("answers");
		
		Ventures venture = new Ventures();
		venture.setAdvantage("");
		venture.setB2bb2c("");
		venture.setBackground("");
		venture.setBlurb("");
		venture.setCaseStudy("");
		venture.setCity("");
		venture.setComments("");
		venture.setCompanyName("");
		venture.setCompetition("");
		venture.setContactName("");
		venture.setEmail("");
		venture.setEmployees("");
		venture.setFounded("");
		venture.setLocation("");
		venture.setMaterials("");
		venture.setPartnerInterests("");
		venture.setPhoneNumber("");
		venture.setPnpContact("");
		venture.setStage("");
		venture.setTags("");
		venture.setTotalMoneyRaised("");
		venture.setVerticals("");
		venture.setWebsite("");
		venture.setPortfolio(new Boolean(false));
		//String timestamp = null;
		String companyName = "";
		String blurb = "";
		String verticals = "";
		String website = "";
		String pnpContact = "";
		String contactName = "";
		String email = "";
		String phoneNumber = "";
		String totalMoneyRaised = "";
		String stage = "";
		String b2bb2c = "";
		String employees = "";
		String location = "";
		String city = "";
		String competition = "";
		String advantage = "";
		String background = "";
		String founded = "";
		String partnerInterests = "";
		String caseStudy = "";
		String comments = "";
		String tags = "";
		String materials = "";
		
		for (int i = 0; i < fields.length(); i++) {
			if(fields.getJSONObject(i).getString("title").contains("Select some tags")){
				tags = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Example of a case study")){
				caseStudy = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("who from PnP")){
				pnpContact = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Name of Company")){
				companyName = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Website")){
				website = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Stage of Company")){
				stage = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Founders' Background")){
				background = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Industrial Applications")){
				verticals = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Any other information or comments")){
				comments = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Email contact")){
				email = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Total Money Raised")){
				totalMoneyRaised = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Specify name of the Country")){
				city = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Competitive Advantage")){
				advantage = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("company founded")){
				founded = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Primary Contact")){
				contactName = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Blurb")){
				blurb = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Closest competition")){
				competition = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("end customer")){
				b2bb2c = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("partners that you would")){
				partnerInterests = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Contact Number")){
				phoneNumber = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Number of Employees")){
				employees = fields.getJSONObject(i).getString("id");
			}
			if(fields.getJSONObject(i).getString("title").contains("Where is your HQ")){
				location = fields.getJSONObject(i).getString("id");
			}
		}
		
		for (int i = 0; i < answers.length(); i++) {
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(tags)){
				List<String> list = new ArrayList<String>();
				for(int j = 0; j < answers.getJSONObject(i).getJSONObject("choices").getJSONArray("labels").length(); j++){
				    list.add(answers.getJSONObject(i).getJSONObject("choices").getJSONArray("labels").getString(j));
				}
				venture.setTags(String.join(",",list));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(caseStudy)){
				venture.setCaseStudy(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(pnpContact)){
				venture.setPnpContact(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(companyName)){
				venture.setCompanyName(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(website)){
				venture.setWebsite(answers.getJSONObject(i).getString("url"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(stage)){
				venture.setStage(answers.getJSONObject(i).getJSONObject("choice").getString("label"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(background)){
				venture.setBackground(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(verticals)){
				List<String> list = new ArrayList<String>();
				for(int j = 0; j < answers.getJSONObject(i).getJSONObject("choices").getJSONArray("labels").length(); j++){
				    list.add(answers.getJSONObject(i).getJSONObject("choices").getJSONArray("labels").getString(j));
				}
				venture.setVerticals(String.join(",",list));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(comments)){
				venture.setComments(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(email)){
				venture.setEmail(answers.getJSONObject(i).getString("email"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(totalMoneyRaised)){
				venture.setTotalMoneyRaised(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(city)){
				venture.setCity(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(advantage)){
				venture.setAdvantage(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(founded)){
				venture.setFounded(answers.getJSONObject(i).getString("date"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(contactName)){
				venture.setContactName(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(blurb)){
				venture.setBlurb(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(competition)){
				venture.setCompetition(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(b2bb2c)){
				List<String> list = new ArrayList<String>();
				for(int j = 0; j < answers.getJSONObject(i).getJSONObject("choices").getJSONArray("labels").length(); j++){
				    list.add(answers.getJSONObject(i).getJSONObject("choices").getJSONArray("labels").getString(j));
				}
				venture.setB2bb2c(String.join(",",list));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(partnerInterests)){
				venture.setPartnerInterests(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(partnerInterests)){
				venture.setPartnerInterests(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(phoneNumber)){
				venture.setPhoneNumber(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(phoneNumber)){
				venture.setPhoneNumber(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(employees)){
				venture.setEmployees(answers.getJSONObject(i).getString("text"));
			}
			if(answers.getJSONObject(i).getJSONObject("field").getString("id").equals(location)){
				venture.setLocation(answers.getJSONObject(i).getJSONObject("choice").getString("label"));
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		venture.setTimestamp(sdf.format(time));

		venture = HibernateUtil.newVenture(venture);
		return Response.ok(venture).header("Access-Control-Allow-Origin", "*").build();
		
	}
	
	@GET
    @Path("/business/{id}")
    @Produces("application/json")
	@Formatted
    public Response getBusinessById(@PathParam("id") int id){
		
		Business business = HibernateUtil.getBusiness(id);
    	System.out.println("Company Info");
    	if(business != null){
    		return Response.ok(business).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).header("Access-Control-Allow-Origin", "*").build();
    	}
		
	}
	
	@GET
    @Path("/businesses/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getAllBusinesses() {
		
    	List<Business> businesess = HibernateUtil.getAllBusinesses();
       	if(!businesess.isEmpty()){
    		return Response.ok(businesess).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NOT_FOUND).entity("Could not find any companies.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
	
	@GET
    @Path("/companies/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Formatted
    public Response getAllCompanies() {
		
    	List<Ventures> ventures = HibernateUtil.getAllVentures();
       	if(!ventures.isEmpty()){
    		return Response.ok(ventures).header("Access-Control-Allow-Origin", "*").build();
    	}else{
    		return Response.status(Response.Status.NO_CONTENT).entity("Could not find any companies.").header("Access-Control-Allow-Origin", "*").build();
    	}        
    }
}
