package pl.mediabit.test.docker;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/ctrl")
public class Controller {

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return "Hello world!" + (0.30000000001 + 0.30000000000000001);
    }

}
