package net.okasian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class ClipboardController {

    @Autowired
    private ClipboardService clipboardService;

    @GetMapping(value = "/poll/{uid}", produces = "text/plain")
    DeferredResult<String> poll(
            @PathVariable("uid") final String uid,
            @RequestParam("ts") final Long ts) {
        return clipboardService.pollData(uid, ts);
    }

    @PostMapping(value = "/put/{uid}", produces = "text/plain", consumes = "text/plain")
    String put(@PathVariable("uid") final String uid,
               @RequestBody final String request) {
        clipboardService.putData(uid, request);
        return "OK";
    }
}
