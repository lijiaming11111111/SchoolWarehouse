package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.item.category.InsertItemCategoryDTO;
import com.school.dto.item.category.PageSelectItemCategoryDTO;
import com.school.dto.item.category.SelectItemCategoryTreeDTO;
import com.school.dto.item.category.UpdateItemCategoryDTO;
import com.school.entity.ItemCategory;
import com.school.exception.BaseException;
import com.school.result.PageResult;
import com.school.vo.item.category.PageSelectItemCategoryVO;
import com.school.vo.item.category.SelectItemCategoryTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.ItemCategoryMapper;
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
        if (itemCategory == null) {
            throw new BaseException("设备分类不存在");
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
        // 用 Map 存所有节点，方便快速查找父节点
        Map<String, SelectItemCategoryTreeVO> nodeMap = new HashMap<>();

        //  初始化所有节点，给 children 赋空数组
        for (SelectItemCategoryTreeVO node : allList) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
        }

        //  遍历所有节点，组装父子关系
        for (SelectItemCategoryTreeVO node : allList) {
            String parentId = node.getParentId();
            // parentId = "0" 是顶级节点，直接加入结果列表
            if ("0".equals(parentId)) {
                treeList.add(node);
            } else {
                // 找到父节点，把当前节点加入父节点的 children
                SelectItemCategoryTreeVO parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                }
            }
        }
        return treeList;
    }
}
