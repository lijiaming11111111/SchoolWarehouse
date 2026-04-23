package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.department.InsertDepartmentDTO;
import com.school.dto.department.PageSelectDepartmentDTO;
import com.school.dto.department.UpdateDepartmentDTO;
import com.school.entity.Department;
import com.school.exception.BaseException;
import com.school.result.PageResult;
import com.school.vo.deparrtment.SelectDepartmentVO;
import com.school.vo.item.flow.PageSelectItemFlowVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.DepartmentMapper;
import server.service.DepartmentService;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    @Override
    public String insertDepartment(InsertDepartmentDTO insertDepartmentDTO) {
        Department department = new Department();
        department.setId(IdWorker.getIdStr());
        department.setName(insertDepartmentDTO.getName());
        departmentMapper.insertDepartment(department);
        return department.getId();
    }

    @Override
    public Boolean updateDepartment(UpdateDepartmentDTO updateDepartmentDTO) {
        if (departmentMapper.selectCountDepartment(updateDepartmentDTO.getId()) == 0) {
            throw new BaseException("部门不存在");
        }
        Department department = new Department();
        department.setId(updateDepartmentDTO.getId());
        department.setName(updateDepartmentDTO.getName());
        departmentMapper.updateDepartment(department);
        return true;
    }

    @Override
    public Boolean deleteDepartment(String id) {
        if (departmentMapper.selectCountDepartment(id) == 0) {
            throw new BaseException("部门不存在");
        }
        departmentMapper.deleteDepartment(id);
        return true;
    }

    @Override
    public PageResult<SelectDepartmentVO> pageSelectDepartment(PageSelectDepartmentDTO pageSelectDepartmentDTO) {
        PageHelper.startPage(pageSelectDepartmentDTO.getPage(), pageSelectDepartmentDTO.getPageSize());
        Page<SelectDepartmentVO> page = departmentMapper.pageQueryDepartment(pageSelectDepartmentDTO);
        return new PageResult<>( page.getTotal(), page.getResult());
    }
}
