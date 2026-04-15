package server.service;

import com.school.dto.warehouse.InsertWarehouseDTO;
import com.school.dto.warehouse.PageSelectWarehouseDTO;
import com.school.dto.warehouse.UpdateWarehouseDTO;
import com.school.result.PageResult;
import com.school.vo.warehouse.SelectWarehouseVO;

import java.util.List;

public interface WarehouseService {
    String insertWarehouse(InsertWarehouseDTO insertWarehouseDTO);

    Boolean updateWarehouse(UpdateWarehouseDTO updateWarehouseDTO);

    Boolean deleteWarehouse(String id);

    PageResult<SelectWarehouseVO> pageSelectWarehouse(PageSelectWarehouseDTO pageSelectWarehouseDTO);
}
