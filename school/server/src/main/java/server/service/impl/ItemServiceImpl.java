package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.item.*;
import com.school.entity.Item;
import com.school.entity.ItemFlow;
import com.school.enums.item.category.ItemStatus;
import com.school.enums.item.flow.FlowType;
import com.school.exception.BaseException;
import com.school.result.PageResult;
import com.school.vo.item.SelectItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.ItemMapper;
import server.service.ItemService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    @Override
    public String insertItem(InsertItemDTO insertItemDTO) {
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
        item.setUseDepartment(insertItemDTO.getUseDepartment());
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
        item.setUseDepartment(updateItemDTO.getUseDepartment());
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
        PageHelper.startPage(pageSelectItemDTO.getPage(), pageSelectItemDTO.getPageSize());
        Page<SelectItemVO> page = itemMapper.pageQueryItem(pageSelectItemDTO);
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
//        itemFlow.setRemark(putItemDTO.getRemark());
        itemFlow.setCreateTime(LocalDate.now());
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
        item.setCurrentStock(itemMapper.selectCountItem(stockItemDTO.getId()) - stockItemDTO.getStockStock());
        ItemFlow itemFlow= new ItemFlow();
        itemFlow.setId(String.valueOf(IdWorker.getId()));
        itemFlow.setItemId(stockItemDTO.getId());
        itemFlow.setItemName(itemMapper.selectItem(stockItemDTO.getId()).getItemName());
        itemFlow.setFlowType(FlowType.STOCK);
        itemFlow.setQuantity(stockItemDTO.getStockStock());
        itemFlow.setBeforeStock(itemMapper.selectCountItem(stockItemDTO.getId()));
        itemFlow.setAfterStock(itemMapper.selectCountItem(stockItemDTO.getId()) - stockItemDTO.getStockStock());
//        itemFlow.setRemark(putItemDTO.getRemark());
        itemFlow.setCreateTime(LocalDate.now());
        itemMapper.insertItemFlow(itemFlow);
        return itemMapper.updateItem(item);
    }
}
