package com.jobapp.jobms.job;


import com.jobapp.jobms.job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> findAll(){
        List<JobDTO> allJobs=jobService.findAll();
        return new ResponseEntity<>(allJobs,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity<>("job added successfully",HttpStatus.CREATED) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id){
        JobDTO jobDTO =jobService.getJobById(id);
        if(jobDTO != null) {
            return new ResponseEntity<>(jobDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delectJob(@PathVariable Long id){
        boolean deleted= jobService.deleteJobById(id);

        if (deleted){
            return new ResponseEntity<>("Job deleted successfully!",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updatedJob){
       boolean updated= jobService.updateJobById(id, updatedJob);
       if (updated){
         return   new ResponseEntity<>("Job updated Successfully",HttpStatus.OK);
       }
       return new ResponseEntity<>("Something went wrong", HttpStatus.NOT_FOUND);
    }


}
