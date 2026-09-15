package com.jobapp.companyms.company;

import com.jobapp.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompanies();
    boolean updatedCompany(Long id, Company updateCompany);
    void createCompany(Company company);
    boolean deletedCompany(Long id);
    Company getCompanyById(Long id);
    public void updateCompanyRating(ReviewMessage reviewMessage);
}
