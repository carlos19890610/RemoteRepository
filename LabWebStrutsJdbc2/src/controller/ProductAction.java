package controller;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import model.ProductBean;
import model.ProductService;

public class ProductAction extends ActionSupport implements RequestAware {
	private ProductBean bean;
	private String prodaction;
	public ProductBean getProduct() {
		return bean;
	}
	public void setProduct(ProductBean bean) {
		this.bean = bean;
	}
	public String getProdaction() {
		return prodaction;
	}
	public void setProdaction(String prodaction) {
		this.prodaction = prodaction;
	}
	@Override
	public void validate() {
		if("Insert".equals(prodaction) || "Update".equals(prodaction) || "Delete".equals(prodaction)) {
			if(bean==null || bean.getId()==0) {
				this.addFieldError("product.id", this.getText("product.id.required"));
			}
		}
	}
	private Map<String, Object> request;
	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	private ProductService productService = new ProductService();
	@Override
	public String execute() throws Exception {
		if("Select".equals(prodaction)) {
			List<ProductBean> result = productService.select(bean);
			request.put("select", result);
			return Action.SUCCESS;
		} else if("Insert".equals(prodaction)) {
			ProductBean result = productService.insert(bean);
			if(result==null) {
				this.addFieldError("action", this.getText("product.insert.failed"));
			} else {
				request.put("insert", result);
			}
			return Action.INPUT;
		} else if("Update".equals(prodaction)) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				this.addFieldError("action", this.getText("product.update.failed"));
			} else {
				request.put("update", result);
			}
			return Action.INPUT;
		} else if("Delete".equals(prodaction)) {
			boolean result = productService.delete(bean);
			if(result) {
				request.put("delete", result);
			} else {
				this.addFieldError("action", this.getText("product.delete.failed"));
			}
			return Action.INPUT;
		} else {
			this.addFieldError("action",
					this.getText("product.action.unknown", new String[] {prodaction}));
			return Action.INPUT;
		}
	}
}
