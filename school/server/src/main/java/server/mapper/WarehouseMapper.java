package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.warehouse.PageSelectWarehouseDTO;
import com.school.entity.Warehouse;
import com.school.vo.warehouse.SelectWarehouseVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WarehouseMapper {

    @Insert("insert into warehouse (id, warehouse_name, warehouse_code, address, user_id, status, remark) " +
            "values (#{id}, #{warehouseName}, #{warehouseCode}, #{address}, #{userId}, #{status}, #{remark})")
    void insertWarehouse(Warehouse warehouse);

    @Select("select * from warehouse where warehouse_code = #{warehouseCode}")
    SelectWarehouseVO selectWarehouseCode(String warehouseCode);

    @Select("select * from warehouse where id = #{id}")
    Warehouse selectWarehouseById(String id);

    void updateWarehouse(Warehouse warehouse);

    @Delete("delete from warehouse where id = #{id}")
    void deleteWarehouse(String id);

    Page<SelectWarehouseVO> pageQueryWarehouse(PageSelectWarehouseDTO pageSelectWarehouseDTO);
}
