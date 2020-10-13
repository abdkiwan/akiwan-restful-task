package com.akiwan.elasticsearch.dao;

import com.akiwan.elasticsearch.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchHit;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Collections;

@Repository
public class UserDao {

    private final String INDEX = "userdata";
    private final String TYPE = "users";

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public UserDao( ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    public User addUser(User user){
        Map<String, Object> dataMap = objectMapper.convertValue(user, Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, user.getId())
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        return user;
    }

    public List<User> getAll(){
        SearchRequest searchRequest = new SearchRequest(); 
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;

        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }

        Map<String, Object> sourceAsMap = null;
        List<User> users = new ArrayList<User>();

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        
        for (SearchHit hit : searchHits) {
            sourceAsMap = hit.getSourceAsMap();
            System.out.println("HELLO");
            String id = (String)sourceAsMap.get("id");
            String firstname = (String)sourceAsMap.get("firstname");
            String lastname = (String)sourceAsMap.get("lastname");
            String email = (String)sourceAsMap.get("email");
            User newuser = new User(id, firstname, lastname, email);
            //System.out.println(newuser);
            //System.out.println(newuser.getClass());
            //System.out.println(users.getClass());
            
            users.add(newuser);
        }

        return users;
    }

    public Map<String, Object> getUserById(String id){
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        return sourceAsMap;
    }

    public Map<String, Object> updateUserById(String id, User user){
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
                .fetchSource(true);    // Fetch Object after its update
        Map<String, Object> error = new HashMap<>();
        error.put("Error", "Unable to update user");
        try {
            String userJson = objectMapper.writeValueAsString(user);
            updateRequest.doc(userJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
            return sourceAsMap;
        }catch (JsonProcessingException e){
            e.getMessage();
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        return error;
    }

    public void deleteUserById(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }

}
