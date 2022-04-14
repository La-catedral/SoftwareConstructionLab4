package resourceType;

import java.util.List;

import exception.otherClientException.IllegalInputException;
import resource.Resource;

public interface MutipleSortedResourceEntry {

	/**
	 * 设置被该对象管理的资源序列
	 * @param thisresource ,被管理的资源序列
	 * @throws IllegalInputException if the input list is empty or not a list of right type of resource
	 */
	public boolean setResource(List<? extends Resource> thisresource) throws IllegalInputException;//防御式编程 防止客户端输入的参数不规范
	
	/**
	 * 获得被该对象管理的资源序列
	 * @return 被管理的资源序列
	 */
	public List<? extends Resource> getResource();
}
