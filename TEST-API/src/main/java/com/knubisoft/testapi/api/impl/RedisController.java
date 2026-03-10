package com.knubisoft.testapi.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.api.RedisApi;
import com.knubisoft.testapi.dto.EmployeeDto;
import com.knubisoft.testapi.exception.ResourceAlreadyExistsException;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${redis.enabled:true}")
public class RedisController implements RedisApi {

    private static final String EMPLOYEE_KEY = "Employee";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;

    @SneakyThrows
    public ResponseEntity<EmployeeDto> putInRedisCache(EmployeeDto employee) {
        boolean exist = redisTemplate.opsForHash().hasKey(EMPLOYEE_KEY, employee.getId());
        if (exist) {
            throw new ResourceAlreadyExistsException(EmployeeDto.class, employee.getId());
        }
        redisTemplate.opsForHash().put(EMPLOYEE_KEY, employee.getId(), objectMapper.writeValueAsString(employee));
        return ResponseEntity.ok(employee);
    }

    public ResponseEntity<List<EmployeeDto>> getEmployeeList() {
        List<EmployeeDto> employeeList = redisTemplate.opsForHash().values(EMPLOYEE_KEY).stream()
                .map(this::mapJsonToEmployee).collect(Collectors.toList());
        return ResponseEntity.ok(employeeList);
    }

    public ResponseEntity<EmployeeDto> getEmployeeById(int id) {
        Object employee = redisTemplate.opsForHash().get(EMPLOYEE_KEY, id);
        if (isNull(employee)) {
            throw new ResourceNotFoundException(EmployeeDto.class, id);
        }
        EmployeeDto employeeDto = mapJsonToEmployee(employee);
        return ResponseEntity.ok(employeeDto);
    }

    public ResponseEntity<String> deleteEmployeeById(int id) {
        Long result = redisTemplate.opsForHash().delete(EMPLOYEE_KEY, id);
        if (isNull(result) || result != 1) {
            throw new ResourceNotFoundException(EmployeeDto.class, id);
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .body(format("Employee with id=%s has been deleted", id));
    }

    @SneakyThrows
    private EmployeeDto mapJsonToEmployee(Object json) {
        return objectMapper.readValue(String.valueOf(json), EmployeeDto.class);
    }
}
