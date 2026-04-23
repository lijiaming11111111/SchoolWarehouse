package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.department.PageSelectDepartmentDTO;
import com.school.entity.Department;
import com.school.vo.deparrtment.SelectDepartmentVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper {

    @Insert("insert into department (id, name) values (#{id}, #{name})")
    void insertDepartment(Department department);
    
    void updateDepartment(Department department);

    @Delete("delete from department where id = #{id}")
    void deleteDepartment(String id);

    @Select("select count(*) from department where id = #{id}")
    int selectCountDepartment(String id);

    Page<SelectDepartmentVO> pageQueryDepartment(PageSelectDepartmentDTO pageSelectDepartmentDTO);

    @Select("select count(*) from department where name = #{departmentName}")
    int selectCountDepartmentName(String departmentName);
}
