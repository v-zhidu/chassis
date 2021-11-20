package io.vzhidu.chassis.common.spring.web;

import io.vzhidu.chassis.common.core.ex.InvalidArgumentException;
import io.vzhidu.chassis.common.core.ex.NotFoundException;
import io.vzhidu.chassis.common.core.ex.PermissionDeniedException;
import io.vzhidu.chassis.common.core.rpc.Response;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Mock controller for test {@link AbstractSpringBootApplication}
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
            throw new NotFoundException("User " + id + " is not found");
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
            throw new InvalidArgumentException("User must not be Mock type");
        }

        return Response.ok(user);
    }

    @GetMapping("ex")
    public Response<String> test() throws Exception {
        throw new Exception("unexpect exception");
    }

    /**
     * Mock interface, get a user with given id
     *
     * @param userId the user id
     * @return {@link MockUser}
     */
    @GetMapping("snake_case")
    public Response<String> testSnakeCase(@RequestParam String userId) {
        return Response.ok(userId);
    }

}
