package com.example.callablecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;

@Controller
@RequestMapping("/async/callable")
public class CallableController {


    @RequestMapping("/response-body")
    public @ResponseBody Callable<String> callable() {
        return () -> {
            Thread.sleep(2000);
            return "Callable result";
        };
    }

    @RequestMapping("/exception")
    public @ResponseBody Callable<String> callableWithException(
        final @RequestParam(required=false, defaultValue="true") boolean handled
    ) {
        return () -> {
            Thread.sleep(2000);
            if (handled) {
                // see handleException method further below
                throw new IllegalStateException("Callable error");
            }
            else {
                throw new IllegalArgumentException("Callable error");
            }
        };
    }

    @RequestMapping("/custom-timeout-handling")
    public @ResponseBody WebAsyncTask<String> callableWithCustomTimeoutHandling() {
        Callable<String> callable = () -> {
            Thread.sleep(2000);
            return "Callable result";
        };

        return new WebAsyncTask<String>(1000, callable);
    }

    @ExceptionHandler
    @ResponseBody
    public String handleException(IllegalStateException ex) {
        return "Handled exception: " + ex.getMessage();
    }

}