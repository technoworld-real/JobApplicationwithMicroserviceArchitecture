package com.jobapp.companyms.company.Impl;



import com.jobapp.companyms.company.Company;
import com.jobapp.companyms.company.CompanyRepository;
import com.jobapp.companyms.company.CompanyService;
import com.jobapp.companyms.company.clients.ReviewClient;
import com.jobapp.companyms.company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    CompanyRepository companyRepository;
    private ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, ReviewClient reviewClient) {
        this.companyRepository = companyRepository;
        this.reviewClient=reviewClient;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public boolean updatedCompany(Long id, Company updateCompany) {

        Optional<Company> companyOptional=companyRepository.findById(id);
        if(companyOptional.isPresent()){
            Company company=companyOptional.get();
            company.setName(updateCompany.getName());
            company.setDescription(updateCompany.getDescription());
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public void createCompany(Company company) {

        companyRepository.save(company);
    }

    @Override
    public boolean deletedCompany(Long id) {
        if(companyRepository.existsById(id)){
            companyRepository.deleteById(id);
            return true;
        } else
            return false;



    }

    @Override
    public Company getCompanyById(Long id) {
       return companyRepository.findById(id).orElse(null);
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {

        System.out.println(reviewMessage.getDescription());
        Company company=companyRepository.findById(reviewMessage.getCompanyId()).orElseThrow(()->
                new NotFoundException("Company not found"+reviewMessage.getCompanyId()));

        double agerageRating=reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(agerageRating);
        companyRepository.save(company);
    }
}
