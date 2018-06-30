package com.ado.moviesub.integrations.tests;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.ado.moviesub.app.entity.user.User;
import com.ado.moviesub.integrations.tests.util.ServerResponse;
import com.ado.moviesub.integrations.tests.util.TestData;

import static org.junit.Assert.*;

public class UserIntegrationTest extends BaseIntegrationTest {

  @Test
  public void testCreateUser() throws IOException {
    User requestUser = TestData.createSampleUser("testCreateUser");

    ServerResponse<User> httpResponse = createUser(requestUser);
    assertEquals(HttpStatus.OK, httpResponse.getStatusCode());

    User responseUser = httpResponse.getResponseObject();

    assertNotNull("Id of user must be generated by the server", responseUser.getId());
    assertEquals(requestUser.getAge(), responseUser.getAge());
    assertEquals(requestUser.getEmail(), responseUser.getEmail());
    assertEquals(requestUser.getUserName(), responseUser.getUserName());
  }

  @Test
  public void testGetUser() throws IOException {
    User sampleUser = TestData.createSampleUser("testGetUser");

    ServerResponse<User> httpResponse = createUser(sampleUser);
    assertEquals(HttpStatus.OK, httpResponse.getStatusCode());

    User createdUser = httpResponse.getResponseObject();
    httpResponse = getUser(createdUser.getId());

    assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
    assertEquals(createdUser, httpResponse.getResponseObject());
  }

  @Test
  public void testUpdateNotExistingUser(){
    User sampleUser = TestData.createSampleUserBuilder("testUpdateNotExistingUser").setId((long) Integer.MAX_VALUE).build();

    ServerResponse<User> httpResponse = updateUser(sampleUser);
    assertEquals(HttpStatus.NOT_FOUND, httpResponse.getStatusCode());
  }

  @Test
  public void testGetNonExistingUser(){
    long unusedId = Integer.MAX_VALUE;

    ServerResponse<User> httpResponse = getUser(unusedId);
    assertEquals(HttpStatus.NOT_FOUND, httpResponse.getStatusCode());
  }

  @Test
  public void testCreateDuplicateUser(){
    User sampleUser = TestData.createSampleUser("testCreateDuplicateUser");

    ServerResponse<User> httpResponse = createUser(sampleUser);
    assertEquals(HttpStatus.OK, httpResponse.getStatusCode());

    ServerResponse<User> duplicateResourceResponse = createUser(sampleUser);
    assertEquals(HttpStatus.CONFLICT, duplicateResourceResponse.getStatusCode());
  }

  @Test
  public void testGetAllUsers() throws IOException {
    User sampleUser = TestData.createSampleUser("testGetAllUsers");

    ServerResponse<User> httpResponse = createUser(sampleUser);
    assertEquals(HttpStatus.OK, httpResponse.getStatusCode());

    ServerResponse<List<User>> httpAllUsersResponse = getUsers();
    assertEquals(HttpStatus.OK, httpAllUsersResponse.getStatusCode());
    assertFalse(httpAllUsersResponse.getResponseObject().isEmpty());
  }
}
