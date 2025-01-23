package io.github.projecthsf.contorller;

import io.github.projecthsf.service.HsfProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.github.projecthsf.binding.HsfRequestBody;
import io.github.projecthsf.binding.provider.HsfProviderProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hsf")
@RequiredArgsConstructor
@Slf4j
public class HsfProviderController {
    private final HsfProviderProperty hsfProviderProperty;
    private final HsfProviderService hsfService;
    @PostMapping
    public Object processRequest(@RequestBody HsfRequestBody requestBody) {
        try {
            Object response = hsfService.processRequest(requestBody);
            log.info("HsfProvider.ProcessRequest success {}", requestBody);
            return response;
        } catch (NoSuchMethodException methodException) {
            log.error("HsfProvider.ProcessRequest {} NoSuchMethodException: {}", requestBody, methodException.getMessage(), methodException);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Method not existed");
        } catch (Exception e) {
            log.error("HsfProvider.ProcessRequest {} Exception {}", requestBody, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ProcessHsf failed: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public List<String> getList() {
        return hsfProviderProperty.getApis();
    }
}
