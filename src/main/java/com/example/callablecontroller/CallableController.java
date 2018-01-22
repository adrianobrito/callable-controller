package com.example.callablecontroller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/async/callable")
public class CallableController {


    @GetMapping("/response-body")
    public Callable<String> callable() {
        return () -> {
            Thread.sleep(2000);
            return "Callable result";
        };
    }

    @GetMapping("/exception")
    public Callable<String> callableWithException(
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

    @GetMapping("/custom-timeout-handling")
    public WebAsyncTask<String> callableWithCustomTimeoutHandling() {
        Callable<String> callable = () -> {
            Thread.sleep(2000);
            return "Callable result";
        };

        return new WebAsyncTask<String>(1000, callable);
    }

    @ExceptionHandler
    public String handleException(IllegalStateException ex) {
        return "Handled exception: " + ex.getMessage();
    }

}