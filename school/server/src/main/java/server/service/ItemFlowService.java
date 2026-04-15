package server.service;

import com.school.dto.item.flow.PageSelectItemFlowDTO;
import com.school.result.PageResult;
import com.school.vo.item.flow.PageSelectItemFlowVO;

public interface ItemFlowService {

    PageResult<PageSelectItemFlowVO> pageSelectItemCategory(PageSelectItemFlowDTO pageSelectItemFlowDTO);
}
