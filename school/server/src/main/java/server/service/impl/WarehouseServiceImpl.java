package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.warehouse.InsertWarehouseDTO;
import com.school.dto.warehouse.PageSelectWarehouseDTO;
import com.school.dto.warehouse.UpdateWarehouseDTO;
import com.school.entity.Warehouse;
import com.school.exception.BaseException;
import com.school.result.PageResult;
import com.school.vo.permission.SelectPermissionIdVO;
import com.school.vo.user.PageQueryUserVO;
import com.school.vo.warehouse.SelectWarehouseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.UserMapper;
import server.mapper.WarehouseMapper;
import server.service.WarehouseService;

@Slf4j
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;

    private final UserMapper userMapper;

    @Override
    public String insertWarehouse(InsertWarehouseDTO insertWarehouseDTO) {
        // 校验仓库编码是否已存在
        SelectWarehouseVO warehouseVO = warehouseMapper.selectWarehouseCode(insertWarehouseDTO.getWarehouseCode());
        if (warehouseVO != null) {
            throw new BaseException("仓库编码已存在");
        }
        if (userMapper.getUserById(Long.valueOf(insertWarehouseDTO.getUserId())) == null) {
            throw new BaseException("负责人不存在");
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(String.valueOf(IdWorker.getId()));
        warehouse.setWarehouseName(insertWarehouseDTO.getWarehouseName());
        warehouse.setWarehouseCode(insertWarehouseDTO.getWarehouseCode());
        warehouse.setAddress(insertWarehouseDTO.getAddress());
        warehouse.setUserId(insertWarehouseDTO.getUserId());
        warehouse.setStatus(insertWarehouseDTO.getStatus());
        warehouse.setRemark(insertWarehouseDTO.getRemark());
        warehouseMapper.insertWarehouse(warehouse);
        return warehouse.getId();
    }

    @Override
    public Boolean updateWarehouse(UpdateWarehouseDTO updateWarehouseDTO) {
        Warehouse warehouse = warehouseMapper.selectWarehouseById(updateWarehouseDTO.getId());
        if (warehouse == null) {
            throw new BaseException("仓库不存在");
        }
        warehouse.setWarehouseName(updateWarehouseDTO.getWarehouseName());
        warehouse.setWarehouseCode(updateWarehouseDTO.getWarehouseCode());
        warehouse.setAddress(updateWarehouseDTO.getAddress());
        warehouse.setUserId(updateWarehouseDTO.getUserId());
        warehouse.setStatus(updateWarehouseDTO.getStatus());
        warehouse.setRemark(updateWarehouseDTO.getRemark());
        warehouseMapper.updateWarehouse(warehouse);
        return true;
    }

    @Override
    public Boolean deleteWarehouse(String id) {
        Warehouse warehouse = warehouseMapper.selectWarehouseById(id);
        if (warehouse == null) {
            throw new BaseException("仓库不存在");
        }
        warehouseMapper.deleteWarehouse(id);
        return true;
    }

    @Override
    public PageResult<SelectWarehouseVO> pageSelectWarehouse(PageSelectWarehouseDTO pageSelectWarehouseDTO) {
        PageHelper.startPage(pageSelectWarehouseDTO.getPage(), pageSelectWarehouseDTO.getPageSize());
        Page<SelectWarehouseVO> page = warehouseMapper.pageQueryWarehouse(pageSelectWarehouseDTO);
        return new PageResult<>( page.getTotal(), page.getResult());
    }


}
