package ru.job4j.auth.service;

import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.EmployeeRepository;
import ru.job4j.auth.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private PersonRepository personRepository;

    public EmployeeService (EmployeeRepository employeeRepository, PersonRepository personRepository) {
        this.employeeRepository = employeeRepository;
        this.personRepository = personRepository;
    }

    public Collection<Employee> findAll() {
        List<Employee> res = new ArrayList<>();
        employeeRepository.findAll().forEach(res::add);
        return res;
    }

    public Employee findById(int id) {
        Employee employee = employeeRepository.findById(id).get();
        List<Person> persons = personRepository.findAll();
        for (Person person : persons) {
            if (person.getEmployeeId() == employee.getId()) {
                employee.addPerson(person);
            }
        }
        return employee;
    }
}
