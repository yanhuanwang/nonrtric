/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.8).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.onap.nearric.simulator.controller;

import org.oransc.ric.a1med.api.model.InlineResponse200;
import org.oransc.ric.a1med.api.model.PolicyTypeSchema;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-04T13:41:42.802+01:00[Europe/Dublin]")
@Api(value = "a1-p", description = "the a1-p API")
public interface A1PApi {

    @ApiOperation(value = "", nickname = "a1ControllerCreateOrReplacePolicyInstance", notes = "Create or replace a policy instance of type policy_type_id. The schema of the PUT body is defined by the create_schema field of the policy type. ", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Policy instance created "),
        @ApiResponse(code = 400, message = "Bad PUT body for this policy instance "),
        @ApiResponse(code = 404, message = "There is no policy type with this policy_type_id ") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}/policies/{policy_instance_id}",
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<Void> a1ControllerCreateOrReplacePolicyInstance(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId,@ApiParam(value = "",required=true) @PathVariable("policy_instance_id") String policyInstanceId,@ApiParam(value = ""  )  @Valid @RequestBody Object body);


    @ApiOperation(value = "", nickname = "a1ControllerCreatePolicyType", notes = "Create a new policy type . Replace is not currently allowed; to replace, for now do a DELETE and then a PUT again. ", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "policy type successfully created"),
        @ApiResponse(code = 400, message = "illegal ID, or object already existed") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}",
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<Void> a1ControllerCreatePolicyType(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId,@ApiParam(value = ""  )  @Valid @RequestBody PolicyTypeSchema body);


    @ApiOperation(value = "", nickname = "a1ControllerDeletePolicyInstance", notes = "Delete this policy instance ", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "policy instance successfully deleted "),
        @ApiResponse(code = 404, message = "there is no policy instance with this policy_instance_id or there is no policy type with this policy_type_id ") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}/policies/{policy_instance_id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> a1ControllerDeletePolicyInstance(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId,@ApiParam(value = "",required=true) @PathVariable("policy_instance_id") String policyInstanceId);


    @ApiOperation(value = "", nickname = "a1ControllerDeletePolicyType", notes = "Delete this policy type. Can only be performed if there are no instances of this type ", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "policy type successfully deleted "),
        @ApiResponse(code = 400, message = "Policy type cannot be deleted because there are instances All instances must be removed before a policy type can be deleted "),
        @ApiResponse(code = 404, message = "policy type not found ") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> a1ControllerDeletePolicyType(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId);


    @ApiOperation(value = "", nickname = "a1ControllerGetAllInstancesForType", notes = "get a list of all policy instance ids for this policy type id", response = String.class, responseContainer = "List", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of all policy instance ids for this policy type id", response = String.class, responseContainer = "List") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}/policies",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<String>> a1ControllerGetAllInstancesForType(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId);


    @ApiOperation(value = "", nickname = "a1ControllerGetAllPolicyTypes", notes = "Get a list of all registered policy type ids", response = Integer.class, responseContainer = "List", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of all registered policy type ids", response = Integer.class, responseContainer = "List") })
    @RequestMapping(value = "/a1-p/policytypes/",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Integer>> a1ControllerGetAllPolicyTypes();


    @ApiOperation(value = "", nickname = "a1ControllerGetHealthcheck", notes = "Perform a healthcheck on a1 ", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A1 is healthy. Anything other than a 200 should be considered a1 as failing ") })
    @RequestMapping(value = "/a1-p/healthcheck",
        method = RequestMethod.GET)
    ResponseEntity<Void> a1ControllerGetHealthcheck();


    @ApiOperation(value = "", nickname = "a1ControllerGetPolicyInstance", notes = "Retrieve the policy instance ", response = Object.class, tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "The policy instance. the schema of this object is defined by the create_schema field of the policy type ", response = Object.class),
        @ApiResponse(code = 404, message = "there is no policy instance with this policy_instance_id or there is no policy type with this policy_type_id ") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}/policies/{policy_instance_id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Object> a1ControllerGetPolicyInstance(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId,@ApiParam(value = "",required=true) @PathVariable("policy_instance_id") String policyInstanceId);


    @ApiOperation(value = "", nickname = "a1ControllerGetPolicyInstanceStatus", notes = "Retrieve the policy instance status across all handlers of the policy ", response = InlineResponse200.class, responseContainer = "List", tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "The policy instance status. Returns a vector of statuses, where each contains a handler_id (opaque id of a RIC component that implements this policy) and the policy status as returned by that handler ", response = InlineResponse200.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "there is no policy instance with this policy_instance_id or there is no policy type with this policy_type_id ") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}/policies/{policy_instance_id}/status",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse200>> a1ControllerGetPolicyInstanceStatus(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId,@ApiParam(value = "",required=true) @PathVariable("policy_instance_id") String policyInstanceId);


    @ApiOperation(value = "", nickname = "a1ControllerGetPolicyType", notes = "Get this policy type ", response = PolicyTypeSchema.class, tags={ "A1 Mediator", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "policy type successfully found", response = PolicyTypeSchema.class),
        @ApiResponse(code = 404, message = "policy type not found ") })
    @RequestMapping(value = "/a1-p/policytypes/{policy_type_id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<PolicyTypeSchema> a1ControllerGetPolicyType(@ApiParam(value = "",required=true) @PathVariable("policy_type_id") Integer policyTypeId);

}
