package ru.job4j.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.service.EmployeeService;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private static final String API = "http://localhost:8080/person/";

    private static final String API_ID = "http://localhost:8080/person/{id}";

    private final EmployeeService employeeService;

    @Autowired
    private RestTemplate rest;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public List<Employee> findAll() {
        List<Employee> rsl = (List<Employee>) employeeService.findAll();
        List<Person> persons = rest.exchange(
                API,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {
                }).getBody();
        for (Employee employee : rsl) {
            for (Person person : persons) {
                if (person.getEmployeeId() == employee.getId()) {
                    employee.addPerson(person);
                }
            }
        }
        return rsl;
    }

    @PostMapping("/")
    public ResponseEntity<Employee> create(@RequestBody Person person) {
        Employee employee = employeeService.findById(person.getEmployeeId());
        if (isNull(employee)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            employee.addPerson(rest.postForObject(API, person, Person.class));
            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        }
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        Employee employee = employeeService.findById(person.getEmployeeId());
        if (isNull(employee)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            rest.put(API, rest.postForObject(API, person, Person.class));
            employee.addPerson(person);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        rest.delete(API_ID, id);
        return ResponseEntity.ok().build();
    }
}

