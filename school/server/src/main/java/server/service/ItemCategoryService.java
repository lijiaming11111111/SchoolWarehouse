package server.service;

import com.school.dto.item.category.InsertItemCategoryDTO;
import com.school.dto.item.category.PageSelectItemCategoryDTO;
import com.school.dto.item.category.SelectItemCategoryTreeDTO;
import com.school.dto.item.category.UpdateItemCategoryDTO;
import com.school.result.PageResult;
import com.school.vo.item.category.PageSelectItemCategoryVO;
import com.school.vo.item.category.SelectItemCategoryTreeVO;

import java.util.List;

public interface ItemCategoryService {
    String insertItemCategory(InsertItemCategoryDTO insertItemCategoryDTO);

    Boolean updateItemCategory(UpdateItemCategoryDTO updateItemCategoryDTO);

    Boolean deleteItemCategory(String id);

    PageResult<PageSelectItemCategoryVO> pageSelectItemCategory(PageSelectItemCategoryDTO pageSelectItemCategoryDTO);

    List<SelectItemCategoryTreeVO> selectItemCategoryTree();
}
