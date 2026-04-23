package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.context.BaseContext;
import com.school.dto.item.*;
import com.school.entity.Item;
import com.school.entity.ItemBorrow;
import com.school.entity.ItemFlow;
import com.school.enums.item.borrow.Status;
import com.school.enums.item.category.ItemStatus;
import com.school.enums.item.flow.FlowType;
import com.school.exception.BaseException;
import com.school.result.PageResult;
import com.school.vo.item.SelectItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.*;
import server.service.ItemService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    private final ItemCategoryMapper itemCategoryMapper;

    private final WarehouseMapper warehouseMapper;

    private final UserMapper userMapper;

    private final ItemBorrowMapper itemBorrowMapper;

    private final DepartmentMapper departmentMapper;

    @Override
    public String insertItem(InsertItemDTO insertItemDTO) {
        if (itemCategoryMapper.selectItemCategoryId(insertItemDTO.getItemCategoryId()) == 0) {
            throw new BaseException("分类ID不存在");
        }
//        if (warehouseMapper.selectWarehouseById(insertItemDTO.getWarehouseId()) == null) {
//            throw new BaseException("仓库ID不存在");
//        }
        if (departmentMapper.selectCountDepartment(insertItemDTO.getDepartmentId()) == 0) {
            throw new BaseException("部门ID不存在");
        }
        Item item = new Item();
        item.setId(String.valueOf(IdWorker.getId()));
        item.setItemName(insertItemDTO.getItemName());
        item.setItemCode(insertItemDTO.getItemCode());
        item.setItemCategoryId(insertItemDTO.getItemCategoryId());
        item.setSpecification(insertItemDTO.getSpecification());
        item.setUnit(insertItemDTO.getUnit());
        item.setWarehouseId(insertItemDTO.getWarehouseId());
        item.setCurrentStock(insertItemDTO.getCurrentStock());
        item.setSafeStock(insertItemDTO.getSafeStock());
        item.setBuyTime(insertItemDTO.getBuyTime());
        item.setBuyPrice(insertItemDTO.getBuyPrice());
        item.setDepartmentId(insertItemDTO.getDepartmentId());
        item.setItemStatus(ItemStatus.NORMAL);
        itemMapper.insertItem(item);
        return item.getId();
    }

    @Override
    public Boolean updateItem(UpdateItemDTO updateItemDTO) {
        Long itemId = itemMapper.selectCountById(updateItemDTO.getId());
        if (itemId == 0L) {
            throw new BaseException("设备不存在");
        }
        Item item = new Item();
        item.setId(updateItemDTO.getId());
        item.setItemName(updateItemDTO.getItemName());
        item.setItemCode(updateItemDTO.getItemCode());
        item.setItemCategoryId(updateItemDTO.getItemCategoryId());
        item.setSpecification(updateItemDTO.getSpecification());
        item.setUnit(updateItemDTO.getUnit());
        item.setWarehouseId(updateItemDTO.getWarehouseId());
        item.setCurrentStock(updateItemDTO.getCurrentStock());
        item.setSafeStock(updateItemDTO.getSafeStock());
        item.setBuyTime(updateItemDTO.getBuyTime());
        item.setBuyPrice(updateItemDTO.getBuyPrice());
        item.setDepartmentId(updateItemDTO.getDepartmentId());
        return itemMapper.updateItem(item);
    }

    @Override
    public Boolean deleteItem(String id) {
        Long itemId = itemMapper.selectCountById(id);
        if (itemId == 0L) {
            throw new BaseException("设备不存在");
        }
        itemMapper.deleteItem(id);
        return true;
    }

    @Override
    public SelectItemVO selectItem(String id) {
        Long itemId = itemMapper.selectCountById(id);
        if (itemId == 0L) {
            throw new BaseException("设备不存在");
        }
        return itemMapper.selectItem(id);
    }

    @Override
    public Boolean updateItemStatus(UpdateItemStatusDTO updateItemStatusDTO) {
        Long itemId = itemMapper.selectCountById(updateItemStatusDTO.getId());
        if (itemId == 0L) {
            throw new BaseException("设备不存在");
        }
        Item item = new Item();
        item.setId(updateItemStatusDTO.getId());
        item.setItemStatus(updateItemStatusDTO.getStatus());
        return itemMapper.updateItem(item);
    }

    @Override
    public PageResult<SelectItemVO> pageSelectItem(PageSelectItemDTO pageSelectItemDTO) {
        String departmentId = userMapper.getUserById(BaseContext.getCurrentUserId()).getDepartmentId();
        PageHelper.startPage(pageSelectItemDTO.getPage(), pageSelectItemDTO.getPageSize());
        Page<SelectItemVO> page = itemMapper.pageQueryItem(pageSelectItemDTO,departmentId);
        return new PageResult<>( page.getTotal(), page.getResult());
    }

    @Override
    public Boolean putItem(PutItemDTO putItemDTO) {
        Long itemId = itemMapper.selectCountById(putItemDTO.getId());
        if (itemId == 0L) {
            throw new BaseException("设备不存在");
        }
        Item item = new Item();
        item.setId(putItemDTO.getId());
        item.setCurrentStock(itemMapper.selectCountItem(putItemDTO.getId()) + putItemDTO.getPutStock());
        ItemFlow itemFlow= new ItemFlow();
        itemFlow.setId(String.valueOf(IdWorker.getId()));
        itemFlow.setItemId(putItemDTO.getId());
        itemFlow.setItemName(itemMapper.selectItem(putItemDTO.getId()).getItemName());
        itemFlow.setFlowType(FlowType.PUT);
        itemFlow.setQuantity(putItemDTO.getPutStock());
        itemFlow.setBeforeStock(itemMapper.selectCountItem(putItemDTO.getId()));
        itemFlow.setAfterStock(itemMapper.selectCountItem(putItemDTO.getId()) + putItemDTO.getPutStock());
        itemFlow.setRemark(putItemDTO.getRemark());
        itemFlow.setCreateTime(LocalDateTime.now());
        itemMapper.insertItemFlow(itemFlow);
        return itemMapper.updateItem(item);
    }

    @Override
    public Boolean stockItem(StockItemDTO stockItemDTO) {
        Long itemId = itemMapper.selectCountById(stockItemDTO.getId());
        if (itemId == 0L) {
            throw new BaseException("设备不存在");
        }
        Item item = new Item();
        item.setId(stockItemDTO.getId());
        item.setCurrentStock(itemMapper.selectCountItem(stockItemDTO.getId()) - stockItemDTO.getStockNumber());
        ItemFlow itemFlow= new ItemFlow();
        itemFlow.setId(String.valueOf(IdWorker.getId()));
        itemFlow.setItemId(stockItemDTO.getId());
        itemFlow.setItemName(itemMapper.selectItem(stockItemDTO.getId()).getItemName());
        itemFlow.setFlowType(FlowType.STOCK);
        itemFlow.setQuantity(stockItemDTO.getStockNumber());
        itemFlow.setBeforeStock(itemMapper.selectCountItem(stockItemDTO.getId()));
        itemFlow.setAfterStock(itemMapper.selectCountItem(stockItemDTO.getId()) - stockItemDTO.getStockNumber());
        itemFlow.setRemark(stockItemDTO.getRemark());
        itemFlow.setCreateTime(LocalDateTime.now());
        itemMapper.insertItemFlow(itemFlow);
        return itemMapper.updateItem(item);
    }

    @Override
    public Boolean borrowItem(BorrowItemDTO borrowItemDTO) {
        //一个人借一次
        Long itemId = itemMapper.selectCountById(borrowItemDTO.getId());
        if (itemId == 0L) {
            throw new BaseException("设备不存在");
        }
        if (borrowItemDTO.getReturnTime().isAfter(LocalDateTime.now())) {
            throw new BaseException("归还时间必须在当前时间之后");
        }
        Long safeStock = itemMapper.selectItem(borrowItemDTO.getId()).getSafeStock();
        if (itemMapper.selectCountItem(borrowItemDTO.getId())-borrowItemDTO.getBorrowNumber() < safeStock) {
            throw new BaseException("借出库存后剩余库存不足安全库存");
        }
        if (borrowItemDTO.getBorrowNumber() <= 0) {
            throw new BaseException("借用数量必须大于0");
        }
//        //判断是否有归还记录，有则不能借出
//        ItemStatus itemStatus = itemMapper.selectItemStatus(borrowItemDTO.getId());
//        if (itemStatus == ItemStatus.BORROW) {
//            throw new BaseException("设备已被借出，不能重复借出");
//        }
//        //判断是否有逾期记录，有则不能借出
//        if (itemStatus == ItemStatus.OVERDUE) {
//            throw new BaseException("设备已被逾期，不能重复借出");
//        }
        //修改设备状态为借用中
        Item item = new Item();
        item.setId(borrowItemDTO.getId());
        item.setCurrentStock(itemMapper.selectCountItem(borrowItemDTO.getId()) - borrowItemDTO.getBorrowNumber());
//        item.setItemStatus(ItemStatus.BORROW);
        itemMapper.updateItem(item);
        //插入借用记录
        ItemBorrow itemBorrow = new ItemBorrow();
        itemBorrow.setId(String.valueOf(IdWorker.getId()));
        itemBorrow.setItemId(borrowItemDTO.getId());
        itemBorrow.setBorrowerUserName(userMapper.getUserById(BaseContext.getCurrentUserId()).getUserName());
        itemBorrow.setBorrowerUserId(String.valueOf(BaseContext.getCurrentUserId()));
        itemBorrow.setQuantity(borrowItemDTO.getBorrowNumber());
        itemBorrow.setBorrowTime(LocalDateTime.now());
        itemBorrow.setReturnTime(borrowItemDTO.getReturnTime());
        itemBorrow.setStatus(Status.BORROW);
        itemBorrow.setRemark(borrowItemDTO.getRemark());
        itemBorrowMapper.insertItemBorrow(itemBorrow);
        //插入库存记录
        ItemFlow itemFlow= new ItemFlow();
        itemFlow.setId(String.valueOf(IdWorker.getId()));
        itemFlow.setItemId(borrowItemDTO.getId());
        itemFlow.setItemName(itemMapper.selectItem(borrowItemDTO.getId()).getItemName());
        itemFlow.setFlowType(FlowType.BORROW);
        itemFlow.setQuantity(borrowItemDTO.getBorrowNumber());
        itemFlow.setBeforeStock(itemMapper.selectCountItem(borrowItemDTO.getId()));
        itemFlow.setAfterStock(itemMapper.selectCountItem(borrowItemDTO.getId()) - borrowItemDTO.getBorrowNumber());
        itemFlow.setRemark(borrowItemDTO.getRemark());
        itemFlow.setCreateTime(LocalDateTime.now());
        itemMapper.insertItemFlow(itemFlow);
        return true;
    }

    @Override
    public Boolean returnItem(ReturnItemDTO returnItemDTO) {
        // 查询借用记录
        ItemBorrow borrow = itemBorrowMapper.selectById(returnItemDTO.getId());
        if (borrow == null) {
            throw new BaseException("借用记录不存在");
        }
        // 只能归还 借用中的
        if (borrow.getStatus() == Status.RETURN) {
            throw new BaseException("该设备已归还，无需重复操作");
        }
        Item item = new Item();
        item.setCurrentStock(item.getCurrentStock() + borrow.getQuantity());
        itemMapper.updateItem(item);

        //  更新借用记录为已归还
        borrow.setStatus(Status.RETURN);
        borrow.setActualReturnTime(LocalDateTime.now());
        itemBorrowMapper.updateItemBorrow(borrow);

        // 插入库存流水（flow_type=4 归还）
        ItemFlow itemFlow= new ItemFlow();
        itemFlow.setItemId(item.getId());
        itemFlow.setItemName(item.getItemName());
        itemFlow.setFlowType(FlowType.RETURN);
        itemFlow.setQuantity(borrow.getQuantity());
        itemFlow.setBeforeStock(item.getCurrentStock());
        itemFlow.setAfterStock(item.getCurrentStock() + borrow.getQuantity());
        itemFlow.setRemark("设备归还");
        itemFlow.setCreateTime(LocalDateTime.now());
        itemMapper.insertItemFlow(itemFlow);
        return true;
    }
}
