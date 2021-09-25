package io.vzhidu.chassis.common.scof;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Github Feign Client
 *
 * @author Zhiqiang Du created at 2021/5/12
 */
@FeignClient(name = "github")
public interface Github {

    @RequestMapping(method = RequestMethod.GET, value = "/repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(@PathVariable("owner") String owner, @PathVariable("repo") String repository);

    @Getter
    @Setter
    @NoArgsConstructor
    class Contributor {
        String login;
        int contributions;
    }
}
