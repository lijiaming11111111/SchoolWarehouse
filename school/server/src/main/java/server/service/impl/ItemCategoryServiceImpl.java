package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.item.category.InsertItemCategoryDTO;
import com.school.dto.item.category.PageSelectItemCategoryDTO;
import com.school.dto.item.category.UpdateItemCategoryDTO;
import com.school.entity.ItemCategory;
import com.school.exception.BaseException;
import com.school.result.PageResult;
import com.school.vo.item.SelectItemVO;
import com.school.vo.item.category.PageSelectItemCategoryVO;
import com.school.vo.item.category.SelectItemCategoryTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.ItemCategoryMapper;
import server.mapper.ItemMapper;
import server.service.ItemCategoryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemCategoryServiceImpl implements ItemCategoryService {

    private final ItemCategoryMapper itemCategoryMapper;

    private final ItemMapper itemMapper;

    @Override
    public String insertItemCategory(InsertItemCategoryDTO insertItemCategoryDTO) {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setId(String.valueOf(IdWorker.getId()));
        itemCategory.setItemCategoryName(insertItemCategoryDTO.getItemCategoryName());
        itemCategory.setParentId(insertItemCategoryDTO.getParentId());
        itemCategoryMapper.insertItemCategory(itemCategory);
        return itemCategory.getId();
    }

    @Override
    public Boolean updateItemCategory(UpdateItemCategoryDTO updateItemCategoryDTO) {
        ItemCategory itemCategory = itemCategoryMapper.selectItemCategoryById(updateItemCategoryDTO.getId());
        if (itemCategory == null) {
            throw new BaseException("设备分类不存在");
        }
        itemCategory.setId(updateItemCategoryDTO.getId());
        itemCategory.setItemCategoryName(updateItemCategoryDTO.getItemCategoryName());
        itemCategory.setParentId(updateItemCategoryDTO.getParentId());
        itemCategoryMapper.updateItemCategory(itemCategory);
        return true;
    }

    @Override
    public Boolean deleteItemCategory(String id) {
        ItemCategory itemCategory = itemCategoryMapper.selectItemCategoryById(id);
        SelectItemVO item = itemMapper.selectItem(id);
        if (itemCategory == null) {
            throw new BaseException("设备分类不存在");
        }
        if (item != null) {
            throw new BaseException("设备分类下有设备，不能删除");
        }
        itemCategoryMapper.deleteItemCategoryById(id);
        return true;
    }

    @Override
    public PageResult<PageSelectItemCategoryVO> pageSelectItemCategory(PageSelectItemCategoryDTO pageSelectItemCategoryDTO) {
        PageHelper.startPage(pageSelectItemCategoryDTO.getPage(), pageSelectItemCategoryDTO.getPageSize());
        Page<PageSelectItemCategoryVO> page = itemCategoryMapper.pageQueryItemCategory(pageSelectItemCategoryDTO);
        return new PageResult<>( page.getTotal(), page.getResult());
    }

    @Override
    public List<SelectItemCategoryTreeVO> selectItemCategoryTree() {
        List<SelectItemCategoryTreeVO> allList = itemCategoryMapper.selectItemCategoryTree();
        List<SelectItemCategoryTreeVO> treeList = new ArrayList<>();
        Map<String, SelectItemCategoryTreeVO> nodeMap = new HashMap<>();
        for (SelectItemCategoryTreeVO node : allList) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
        }

        for (SelectItemCategoryTreeVO node : allList) {
            String parentId = node.getParentId();
            if ("0".equals(parentId)) {
                treeList.add(node);
            } else {
                SelectItemCategoryTreeVO parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                }
            }
        }
        return treeList;
    }
}
