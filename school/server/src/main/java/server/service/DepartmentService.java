package server.service;

import com.school.dto.department.InsertDepartmentDTO;
import com.school.dto.department.PageSelectDepartmentDTO;
import com.school.dto.department.UpdateDepartmentDTO;
import com.school.result.PageResult;
import com.school.vo.deparrtment.SelectDepartmentVO;

public interface DepartmentService {
    String insertDepartment(InsertDepartmentDTO insertDepartmentDTO);

    Boolean updateDepartment(UpdateDepartmentDTO updateDepartmentDTO);

    Boolean deleteDepartment(String id);

    PageResult<SelectDepartmentVO> pageSelectDepartment(PageSelectDepartmentDTO pageSelectDepartmentDTO);
}
