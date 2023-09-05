package com.example.testspring.services;

import com.example.testspring.dto.DepartmentDTO;
import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.dto.UserDTO;
import com.example.testspring.entity.Department;
import com.example.testspring.entity.User;
import com.example.testspring.repository.DepartmentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface DepartmentServices {
    void create(DepartmentDTO departmentDTO);

    DepartmentDTO update(DepartmentDTO departmentDTO);

    void delete(int id);

    List<DepartmentDTO> readAll();

    DepartmentDTO getById(int id);

    PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO);
}
@Service
class DepartmentServicesImpl implements DepartmentServices{
    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    CacheManager cacheManager;
    @Override
    @Transactional
    @CacheEvict (cacheNames = "department-search",allEntries = true)
    public void create(DepartmentDTO departmentDTO) {
        //Viet thế này đỡ phải set
        Department department = new ModelMapper().map(departmentDTO,Department.class);
        departmentRepo.save(department);
        Cache cache =
                cacheManager.getCache("department-search");
        cache.invalidate();
//        cache.put(department,cache);
    }

    //Cair thieenj toc do truy van o backend
    // Nếu dùng void để update thì  dùng @CacheEvict
    // Nếu trả về là 1 type thì nên dùng @CachePut return về
    @Override
    @Transactional
    @Caching(
            evict = {
            @CacheEvict (cacheNames = "department-search",allEntries = true)
            },
            put = {
                    @CachePut(cacheNames = "department",key = "#departmentDTO.id")
            }
    )
    @CachePut(cacheNames = "department",key = "#departmentDTO.id")
    public DepartmentDTO update(DepartmentDTO departmentDTO) {
        Department currentDepartment = departmentRepo.findById(departmentDTO.getId()).orElse(null);
        if (currentDepartment!=null){
            currentDepartment.setName(departmentDTO.getName());
            departmentRepo.save(currentDepartment);
        }
        return convert(currentDepartment);
    }

    @Override
    @Transactional
    @Caching(evict = {
    @CacheEvict(cacheNames = "department",key = "#id"),
    @CacheEvict(cacheNames = "department2",allEntries = true)
    })
    public void delete(int id) {
        departmentRepo.deleteById(id);
    }

    @Override
    @Transactional
    public List<DepartmentDTO> readAll() {
        List<Department> departments = departmentRepo.findAll();
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        for (Department department : departments){
            departmentDTOList.add(convert(department));
        }
        return departmentDTOList;
    }

    @Override
    @Cacheable(cacheNames = "department",key = "#id",unless = "#result == null ")
    public DepartmentDTO getById(int id) {
//        System.out.println("Chua co CACHE");
        Department department = departmentRepo.findById(id)
                        .orElseThrow(NoResultException::new);
//        List<User> users = department.getUsers();
//        System.out.println(users.size());
        return convert(department);
    }
    public DepartmentDTO convert(Department department){
        return new ModelMapper().map(department, DepartmentDTO.class);
    }

    @Override
    @Cacheable(cacheNames = "department-search")
    public PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO) {
        Sort sortBy = Sort.by("name").ascending();
        if (StringUtils.hasText(searchDTO.getSortedField())){
            sortBy=  Sort.by(searchDTO.getSortedField()).descending();
        }
        if (searchDTO.getCurrentPage()==null){
            searchDTO.setCurrentPage(0);
        }
        if (searchDTO.getSize()==null){
            searchDTO.setSize(5);
        }
//        if (searchDTO.getKeyword()==null){
//            searchDTO.setKeyword("");
//        }
        PageRequest pageRequest =
                PageRequest.of(searchDTO.getCurrentPage(),searchDTO.getSize(),sortBy);
        Page<Department> page = departmentRepo.searchName("%"+searchDTO.getKeyword()+"%", pageRequest);

        PageDTO<List<DepartmentDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<DepartmentDTO> departmentDTOS =
                page.get().map(u -> convert(u)).collect(Collectors.toList());
        pageDTO.setData(departmentDTOS);
        return pageDTO;
    }
}
