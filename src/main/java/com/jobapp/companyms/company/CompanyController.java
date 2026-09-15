package com.jobapp.companyms.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping()
    public ResponseEntity<List<Company>> getCompany(){
    List<Company> company=companyService.getAllCompanies();
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> createCompany(@RequestBody Company company){
        companyService.createCompany(company);
        return new ResponseEntity<>("Company added successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany){
        boolean updated=companyService.updatedCompany(id,updatedCompany);
        if(updated){
           return new  ResponseEntity<>("Company updated successfully!",HttpStatus.CREATED);
        }
        else
           return new ResponseEntity<>("Something went wrong!",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id){
        boolean deleted=companyService.deletedCompany(id);
        if(deleted){
          return new   ResponseEntity<>("Company deleted successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company companybyid = companyService.getCompanyById(id);
        if (companybyid != null) {
            return new ResponseEntity<Company>(companybyid, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
