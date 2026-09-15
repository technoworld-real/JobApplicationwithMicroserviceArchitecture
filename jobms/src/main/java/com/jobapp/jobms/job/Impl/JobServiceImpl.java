package com.jobapp.jobms.job.Impl;


import com.jobapp.jobms.job.Job;
import com.jobapp.jobms.job.JobRepository;
import com.jobapp.jobms.job.JobService;
import com.jobapp.jobms.job.clients.CompanyClient;
import com.jobapp.jobms.job.clients.ReviewClient;
import com.jobapp.jobms.job.dto.JobDTO;
import com.jobapp.jobms.job.external.Company;
import com.jobapp.jobms.job.external.Review;
import com.jobapp.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private RestTemplate restTemplate;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;
   // private List<Job> jobs=new ArrayList<>();
    JobRepository jobRepository;
    private Long nextId=1L;
    int attempt=0;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient,
                          ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient=companyClient;
        this.reviewClient=reviewClient;
    }

    @Override
    @RateLimiter(name = "companyBreaker")
   // @Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
   // @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("attempt:"+ ++attempt);
        List<Job> jobs=jobRepository.findAll();
        List<JobDTO> jobDTOList =new ArrayList<>();

        for(Job job:jobs){

            JobDTO jobDTO =new JobDTO();
           // jobWithCompanyDTO.setJob(job);

            Company company=  restTemplate.getForObject("http://COMPANY-SERVICE/companies/"+job.getCompany(), Company.class);
            jobDTO.setCompany(company);

            jobDTOList.add(jobDTO);

        }

       return jobs.stream().map(this::convertToDto)
               .collect(Collectors.toList());

    }

    public List<String> companyBreakerFallback(){
        List<String> list=new ArrayList<>();
        list.add("dummy");
        return list;
    }

    private JobDTO convertToDto(Job job){

        Company company=companyClient.getCompany(job.getCompany());
        List<Review> reviews=reviewClient.getReviews(job.getCompany());


        /* below code using restTemplate*/
           // Company company=  restTemplate.getForObject("http://company-service/companies/"+job.getCompany(),Company.class);
         /* ResponseEntity<List<Review>> reviewResponse= restTemplate.exchange("http://review-service/reviews?companyId=" + job.getCompany(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
                    });
          List<Review> reviews=reviewResponse.getBody();*/

            JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(
                    job,company,reviews);
          //  jobDTO.setCompany(company);

            return jobDTO;


    }

    @Override
    public void createJob( Job job) {
       // job.setId(nextId++);
        jobRepository.save(job);
       // jobs.add(job);

    }

    @Override
    public JobDTO getJobById(Long id) {
      Job job=   jobRepository.findById(id).orElse(null);
      return convertToDto(job);
//        for(Job job:jobs){
//            if(job.getId().equals(id)){
//                return job;
//            }
//        }

      //  return null;
    }

    @Override
    public boolean deleteJobById(Long id) {
       try {
           jobRepository.deleteById(id);
           return true;
       } catch (Exception e) {
           return false;
       }

       // Iterator<Job> iterator= jobs.iterator();
        //while(iterator.hasNext()){
          //  Job job=iterator.next();
            //if(job.getId().equals(id)){
              //  iterator.remove();
                //return true;
           // }


       // }
        //return false;
    }

    @Override
    public boolean updateJobById(Long id, Job updatedJob) {
        Optional<Job> jobOptional=jobRepository.findById(id);

            if(jobOptional.isPresent()){
                Job job=jobOptional.get();
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setLocation(updatedJob.getLocation());
                jobRepository.save(job);
                return true;
            }

        return false;
    }
}
