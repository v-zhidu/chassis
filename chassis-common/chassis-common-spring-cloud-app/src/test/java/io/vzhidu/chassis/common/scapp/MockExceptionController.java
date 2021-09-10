package io.vzhidu.chassis.common.scapp;

import io.vzhidu.chassis.common.core.ex.InvalidParameterException;
import io.vzhidu.chassis.common.core.ex.PermissionDeniedException;
import io.vzhidu.chassis.common.core.ex.ResourceNotFoundException;
import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Mock controller for test {@link AbstractChassisSpringBootApplication}
 *
 * @author Zhiqiang Du Created at 2019/12/30
 */
@RestController
@RequestMapping("mock")
public final class MockExceptionController {

    /**
     * Mock interface, get a user with given id
     *
     * @param id the user id
     * @return {@link MockUser}
     */
    @GetMapping("get")
    public Response<MockUser> get(@RequestParam("id") String id) {
        if (id.equals("0")) {
            throw new PermissionDeniedException("Permission denied on resource '0' of user");
        }
        if (id.equals("-1")) {
            throw new ResourceNotFoundException("User " + id + " is not found");
        }

        MockUser fakeUser = new MockUser(id, MockUser.MockUserType.MOCK, "fake_user", null);
        return Response.ok(fakeUser);
    }

    /**
     * Mock interface, create a fake user
     *
     * @param user {@link MockUser}
     * @return {@link Response}
     */
    @PostMapping("post")
    public Response<MockUser> post(@Valid @RequestBody MockUser user) {
        if (user.getType() == MockUser.MockUserType.MOCK) {
            throw new InvalidParameterException("User must not be Mock type");
        }

        return Response.ok(user);
    }

    @GetMapping("ex")
    public Response<String> test() throws Exception {
        throw new Exception("unexpect exception");
    }
}
