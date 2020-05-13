package com.company.project.utils.erp;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.utils.web.SSLClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class ERPHttpClient {

	private static final Logger log = LoggerFactory.getLogger(ERPHttpClient.class);
	public static final String title = "the ERP API message is wrong in {}";
	private CloseableHttpClient erpHttp;
	private HttpClientContext erpContext;


	private String acctID="5cb48b56a6400a";

	private String username="吴方涛";

	private String password="123456";

	private String lcid="2052";


	private void reCreat() {

		this.erpHttp = SSLClient.getHttpsClient();
		this.erpContext = HttpClientContext.create();

		JSONObject json = new JSONObject();
		json.put("acctID", acctID);
		json.put("username", username);
		json.put("password", password);
		json.put("lcid", lcid);

		CloseableHttpResponse response = null;

		/**
		 * 这里是主程序
		 */
		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc");
		post.addHeader("Content-Type", "application/json");

		try {
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			response = erpHttp.execute(post, erpContext);
			post.addHeader("Content-type", "application/json;charset=UTF-8");
			HttpEntity entity = response.getEntity();

			// 这里是返回
			String message = EntityUtils.toString(entity);
			if (message != null && message.startsWith("{")
					&& JSONObject.parseObject(message).getIntValue("LoginResultType") == 1) {
				log.info("二次登录成功");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private ERPHttpClient() {

		this.erpHttp = SSLClient.getHttpsClient();
		this.erpContext = HttpClientContext.create();

		JSONObject json = new JSONObject();
		json.put("acctID", acctID);
		json.put("username", username);
		json.put("password", password);
		json.put("lcid", lcid);
		CloseableHttpResponse response = null;

		/**
		 * 这里是主程序
		 */
		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc");
		post.addHeader("Content-Type", "application/json");

		try {
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			response = erpHttp.execute(post, erpContext);
			post.addHeader("Content-type", "application/json;charset=UTF-8");
			HttpEntity entity = response.getEntity();

			// 这里是返回
			String message = EntityUtils.toString(entity);
			System.out.println("登录结果反馈" + message);
			if (message != null && message.startsWith("{")
					&& JSONObject.parseObject(message).getIntValue("LoginResultType") == 1) {
				log.info("第一次登录成功");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private static class SingletonHolder {
		private static final ERPHttpClient sInstance = new ERPHttpClient();
	}

	/**
	 * 
	 * @param selectParamjson ，格式必须为{"formid":"","data":{"CreateOrgId":"100011","Id":"",
	 *               "Number": ""}},其中formid为要查询类型的，id非必填，number必填,就是id
	 * @return
	 */
	public String selectForms(String selectParamjson) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new StringEntity(selectParamjson));
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
			if (message.startsWith("{")) {
				JSONObject j = JSONObject.parseObject(message);

				if ((j.containsKey("Result") && j.getJSONObject("Result").containsKey("ResponseStatus")
						&& j.getJSONObject("Result").get("ResponseStatus") != null) || message.contains("重新登录")) {
					log.error("第一次调用失败，请求调用第二次");
					reCreat();
					return selectFormsSecond(selectParamjson);
				} else {
					return message;
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return null;
	}

	public String selectFormsSecond(String selectParamjson) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new StringEntity(selectParamjson));
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);

			return message;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return null;
	}


	public static ERPHttpClient getInstance() {

		return SingletonHolder.sInstance;
	}


	public static ERPHttpClient getNewInstance() {

		return new ERPHttpClient();
	}

	/**
	 * 批量查询接口
	 * 
	 * @param selectParamjson
	 */
	public String selectBitchForms(String selectParamjson) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc");

		CloseableHttpResponse response = null;
		try {
			JSONObject json = new JSONObject();
			json.put("format", 1);
			json.put("timestamp", new Date().toString());
			json.put("v", "1.0");
			json.put("parameters", selectParamjson);
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			post.addHeader("Content-type", "application/json;charset=UTF-8");
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
			if (!message.startsWith("[") || message.contains("请重新登录")) {
				log.error("第一次调用失败，请求调用第二次");
				reCreat();
				return selectBitchFormsSecond(selectParamjson);
			} else {
				return message;
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return null;
	}

	/**
	 * 用于请求失败后，批量查询接口
	 * 
	 * @param
	 */
	public String selectBitchFormsSecond(String selectParamjson) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc");

		CloseableHttpResponse response = null;
		try {
			JSONObject json = new JSONObject();
			json.put("format", 1);
			json.put("timestamp", new Date().toString());
			json.put("v", "1.0");
			json.put("parameters", selectParamjson);
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			post.addHeader("Content-type", "application/json;charset=UTF-8");
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);

			return message;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return null;
	}

	/**
	 * erp提交接口
	 * 
	 * @param deleteData
	 */

	public JSONObject submitForms(String deleteData) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new StringEntity(deleteData));
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * erp审核接口
	 * 
	 * @param deleteData
	 */

	public JSONObject auditForms(String deleteData) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new StringEntity(deleteData));
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * erp反审核接口
	 * 
	 * @param deleteData
	 */

	public JSONObject unauditForms(String deleteData) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new StringEntity(deleteData));
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * erp删除接口
	 * 
	 * @param
	 */

	public JSONObject deleteParams(String deleteData) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Delete.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new StringEntity(deleteData));
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * erp暂存接口
	 * 
	 * @param deleteData
	 */

	public JSONObject draftParams(String deleteData) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Draft.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new StringEntity(deleteData));
			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
			return JSONObject.parseObject(message);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	public static void main(String[] args) {

		// //发货通知
		// String data = "{\"FormId\": \"BD_Customer\", \"data\":
		// {\"CreateOrgId\": \"0\",\"Number\": \"\", \"Id\": \"\"}}";
		// String queryString = "[{\"FID
		// >\":\"100000\",\"FormId\":\"SAL_DELIVERYNOTICE\",\"FieldKeys\":\"FID,FDate,FBillNo,FCustomerID,FDeliveryDeptID,FDocumentStatus,FMaterialID,FMaterialName,FUnitID,FQty,FCloseStatus,FSalesManID,FHeadDeliveryWay,FSaleDeptID\",\"FilterString\":\"\",\"OrderString\":\"\",\"TopRowCount\":0,\"StartRow\":0,\"Limit\":0}]";
		// ERPHttpClient client = ERPHttpClient.getInstance();
		// String s = "2019-04-02";

		// FID,FDate,FBillNo,
		// FBUSINESSTYPE, 业务类型
		// FBILLTYPEID, 单据类型
		// FDOCUMENTSTATUS, 单据状态
		// FPURCHASERID 采购员
		// FSUPPLIERID 供应商
		// FMaterialName 物料名称
		// FMATERIALID 物料编码
		// FUNITID 单位
		// FREALQTY 数量
		// FLOT 批号
		// FSTOCKID 仓库
		// FSTOCKLOCID 仓位

		// //采购入库单列表
		// String queryString =
		// "[{\"FormId\":\"STK_InStock\",\"FieldKeys\":\"FID,FDate,FBillNo,FBUSINESSTYPE,FBILLTYPEID,FDOCUMENTSTATUS,FPURCHASERID,FSUPPLIERID,FMaterialName,FMATERIALID,FUNITID,FREALQTY,FLOT,FSTOCKID,FSTOCKLOCID
		// \",\"FilterString\":\"\",\"OrderString\":\"\",\"TopRowCount\":0,\"StartRow\":0,\"Limit\":0}]";
		// ERPHttpClient client = ERPHttpClient.getInstance();

		ERPHttpClient client = ERPHttpClient.getInstance();
		// //采购订单列表
		// String queryString =
		// "[{\"FormId\":\"PUR_PurchaseOrder\",\"FieldKeys\":\"FID,FBillNo,FDocumentStatus,FDate,FPurchaseOrgId,FPurchaserGroupId,FCloseStatus,FPurchaserId,FBillTypeID,FSettleId,FChargeId,FProviderId,
		// FPOOrderEntry_FEntryID,FMaterialId,FQty,FPrice,FEntryTaxRate,FEntryNote,FModel,FUnitId,F_abcd_xsglsl
		// \",\"FilterString\":\"\",\"OrderString\":\"\",\"TopRowCount\":0,\"StartRow\":0,\"Limit\":0}]";
		// String string = client.selectBitchForms(queryString);
		// System.out.println( string );

		// 实体主键：FID
		// 单据编号：FBillNo
		// 单据状态：FDocumentStatus
		// 采购日期：FDate (必填项)
		// 采购组织：FPurchaseOrgId (必填项)
		// 供应商：FSupplierId (必填项)
		// 采购组：FPurchaserGroupId
		// 采购部门：FPurchaseDeptId
		// 创建人：FCreatorId
		// 创建日期：FCreateDate
		// 最后修改人：FModifierId
		// 最后修改日期：FModifyDate
		// 审核人：FApproverId
		// 审核日期：FApproveDate
		// 作废人：FCancellerId
		// 作废日期：FCancelDate
		// 作废状态：FCancelStatus
		// 关闭状态：FCloseStatus
		// 采购员：FPurchaserId (必填项)
		// 关闭人：FCloserId
		// 关闭日期：FCloseDate
		// 单据类型：FBillTypeID (必填项)
		// 结算方：FSettleId
		// 收款方：FChargeId
		// 供货方：FProviderId
		// 变更人：FChangerId
		// 版本号：FVersionNo
		// 变更日期：FChangeDate
		// 变更原因：FChangeReason
		// 业务类型：FBusinessType
		// 供货方地址：FProviderAddress
		// 指定供应商：FAssignSupplierId
		// 对应组织：FCorrespondOrgId
		// 供货方联系人(旧)：FProviderContact
		// 修改变更操作：FIsModificationOperator
		// 供应商协同平台订单编号(6.1废弃)：FNetOrderBillNo
		// 供应商协同平台订单ID(6.1废弃)：FNetOrderBillId
		// 确认状态：FConfirmStatus
		// 确认人：FConfirmerId
		// 确认日期：FConfirmDate
		// 供货方联系人：FProviderContactId
		// 职务：FProviderJob
		// 手机：FProviderPhone
		// 源单编号：FSourceBillNo
		// 货款结算日期：F_abcd_Date
		// 收货地址：F_abcd_Remarksshdz
		// 渠道：F_PREH_qdAssistant
		// 采购订单类型：F_ABCD_Combo
		// 交货方式：FHeadDeliveryWay
		// 签约地点：F_ABCD_qydd
		// 预订单日期：F_ABCD_Date1
		// 财务信息：FPOOrderFinance
		// 实体主键：FEntryId
		// 整单费用：FBillCost
		// 金额：FBillAmount
		// 价税合计：FBillAllAmount
		// 价目表：FPriceListId
		// 折扣表：FDiscountListId
		// 结算币别：FSettleCurrId (必填项)
		// 税额：FBillTaxAmount
		// 定价时点：FPriceTimePoint (必填项)
		// 本位币：FLocalCurrId
		// 金额(本位币)：FBillAmount_LC
		// 税额(本位币)：FBillTaxAmount_LC
		// 价税合计(本位币)：FBillAllAmount_LC
		// 汇率类型：FExchangeTypeId
		// 汇率：FExchangeRate
		// 付款条件：FPayConditionId
		// 预付单号：FPayAdvanceBillId
		// 含税：FIsIncludedTax
		// 结算方式：FSettleModeId
		// 预付已核销金额：FPreMatchAmountFor
		// 集中结算(6.0废弃)：FSEPSETTLE
		// 单次预付额度：FPAYADVANCEAMOUNT
		// 单次预付额度汇率：FSupToOderExchangeBusRate
		// 集中结算组织：FFOCUSSETTLEORGID
		// 价外税：FISPRICEEXCLUDETAX
		// 保证金比例%：FDepositRatio
		// 保证金：FDeposit
		// 关联保证金：FRelateDeposit
		// 申请关联保证金：FAPPLYRELATEDEPOSIT
		// 关联退款保证金：FRelateRefundDeposit

		// String
		// selectParam="[{\"FormId\": \"SAL_OUTSTOCK\",\"FieldKeys\":
		// \"Fid,FBillAmount,FEntryCostAmount,F_ABCD_khjl,FSALEDEPTID.fname,FSALESMANID.fname,Fdate\",\"FilterString\":\"FEntryCostAmount
		// > 0 and FDate >= '"+s+" '\",\"OrderString\": \"\",\"TopRowCount\":
		// 0,\"StartRow\": 0,\"Limit\": 0}]";
		// String
		// selectParam="[{\"FormId\": \"SAL_OUTSTOCK\",\"FieldKeys\":
		// \"(FPrice-FCostPrice)*FSALUNITQTY,F_ABCD_khjl,FSALEDEPTID.fname,FSALESMANID.fname\",\"FilterString\":\"FEntryCostAmount
		// > 0 and FDate between '2019-04-01' and
		// '2019-04-30'\",\"OrderString\": \"\",\"TopRowCount\": 0,\"StartRow\":
		// 0,\"Limit\": 0}]";

		// String
		// select="[{\"FormId\": \"SAL_OUTSTOCK\",\"FieldKeys\":
		// \"FAmount,FCostAmount_LC,FBillNo\",\"FilterString\":\"FSALEDEPTID.fname
		// like '%电商%' and FDate between '2019-04-01' and '2019-04-30' and
		// FCostPrice > 0\",\"OrderString\": \"\",\"TopRowCount\":
		// 0,\"StartRow\": 0,\"Limit\": 0}]";

		// String
		// cont="[{\"FormId\": \"SAL_OUTSTOCK\",\"FieldKeys\":
		// \"sum(FSALUNITQTY-FRETURNQTY) as x,sum(FBILLAMOUNT_LC-0) as
		// y\",\"FilterString\": \"FSaleDeptID.fname like '%钢材%' and
		// FDocumentStatus='C'\",\"OrderString\": \"\",\"TopRowCount\":
		// 0,\"StartRow\": 0,\"Limit\": 0}]";

		// String string=client.selectBitchForms(cont);

		// System.out.println(string);
		// if(string!=null&&string.startsWith("[")&&string.endsWith("]")){
		//
		// XssFUtils xs= XssFUtils.getInstance();
		// String name="aaaaa.xls";
		// OutputStream stream=new FileOutputStream("D://"+name);
		// xs.ToConvertExcle(JSONArray.fromObject(string), stream, name);
		//
		//
		// }

		// ERPHttpClient client = ERPHttpClient.getInstance();

		// String dd_no="XD012019062000029";
		// String selectParam="[{\"FormId\": \"SAL_SaleOrder\",\"FieldKeys\":
		// \"FID,FBillNo,FSettleModeId.fname,FRecConditionId.fname\",\"FilterString\":
		// \"FBillNo ='"+dd_no+"'\",\"OrderString\": \"Fid
		// asc\",\"TopRowCount\": 0,\"StartRow\": 0,\"Limit\": 0}]";
		// String string = client.selectBitchForms(selectParam);
		// System.out.println("查 "+string);

		// 采购订单
		String cg_no = "CG2019.05.141143";
		String queryString1 = "[{\"FormId\":\"PUR_PurchaseOrder\",\"FieldKeys\":\"FID,FPOOrderEntry_FEntryID,FDate,FBillNo \",\"FilterString\":\"FBillNo ='"
				+ cg_no + "'\",\"OrderString\":\"\",\"TopRowCount\":0,\"StartRow\":0,\"Limit\":0}]";
		ERPHttpClient client2 = ERPHttpClient.getInstance();
		String ss1 = client2.selectBitchForms(queryString1);
		System.out.println("ss1:" + ss1);
		JSONArray jrr = JSONArray.parseArray(ss1);

		// 销售订单 反写
		// 业务对象Id
		String sFormId = "SAL_SaleOrder"; // 销售出库单
		String sContentss = "{" + "    \"Creator\": \"\"," + "    \"NeedUpDateFields\": [],"
				+ "    \"NeedReturnFields\": []," + "    \"IsDeleteEntry\": \"true\"," + "    \"SubSystemId\": \"\","
				+ "    \"IsVerifyBaseDataField\": \"true\"," + "    \"IsEntryBatchFill\": \"true\","
				+ "    \"ValidateFlag\": \"true\"," + "    \"NumberSearch\": \"true\","
				+ "    \"InterationFlags\": \"\"," + "    \"IsAutoSubmitAndAudit\": \"false\"," + "    \"Model\": {"
				+ "        \"FID\": 0," + "        \"FBillTypeID\": {" + // 单据类型
				"            \"FNUMBER\": \"XSDD01_SYS\"" + "        }," + "        \"FDate\": \"2019-06-20 00:00:00\","
				+ "        \"FSaleOrgId\": {" + // 销售组织
				"            \"FNumber\": \"01\"" + "        }," + "        \"FCustId\": {" + // 客户
				"            \"FNumber\": \"01\"" + "        }," + "        \"FHeadDeliveryWay\": {" + // 交货方式
				"            \"FNumber\": \"JHFS01_SYS\"" + "        }," + "        \"FReceiveId\": {" + // 收货方
				"            \"FNumber\": \"01\"" + "        }," + "        \"FCorrespondOrgId1\": {" + // 本组织
				"            \"FNumber\": \"01\"" + "        }," + "        \"FCorrespondOrgId\": {" + // 对应组织
				"            \"FNumber\": \"01\"" + "        }," + "        \"FSaleDeptId\": {"
				+ "            \"FNumber\": \"01.3\"" + // 销售部门
				"        }," + "        \"FSalerId\": {" + // 销售员
				"            \"FNumber\": \"0050\"" + "        }," + "        \"FSettleId\": {" + // 结算方
				"            \"FNumber\": \"01\"" + "        }," + "        \"FChargeId\": {" + // 付款方
				"            \"FNumber\": \"01\"" + "        }," + "        \"FISINVOICEARLIER\": false," + // 先到票后出库
				"        \"FISINIT\": false," + // 是否期初单据
				"        \"FIsMobile\": false," + // 来自移动
				"        \"F_PREH_qdAssistant\": {" + // 渠道
				"            \"FNumber\": \"QD002\"" + "        }," + "        \"F_PREH_gys1\": {" + // 内部供应商
				"            \"FNUMBER\": \"NBCG000001\"" + "        }," + "        \"F_ABCD_YJ\": \"1\"," + // YJ
				"        \"FSaleOrderFinance\": {" + // 财务信息
				"            \"FSettleCurrId\": {" + "                \"FNumber\": \"PRE001\"" + // 结算币别
				"            }," + "            \"FRecConditionId\": {" + // 收款条件
				"                \"FNumber\": \"064\"" + "            }," + "            \"FIsPriceExcludeTax\": true,"
				+ // 价外税
				"            \"FSettleModeId\": {" + "                \"FNumber\": \"JSFS01_SYS\"" + // 结算方式
				"            }," + "            \"FIsIncludedTax\": true," + // 是否含税
				"            \"FExchangeTypeId\": {" + // 汇率类型
				"                \"FNumber\": \"HLTX01_SYS\"" + "            },"
				+ "            \"FOverOrgTransDirect\": false" + // 寄售生成跨组织调拨
				"        }," + "        \"FSaleOrderEntry\": [" + // 订单明细
				"            {" + "                \"FRowType\": \"Standard\"," + // 产品类型
				"                \"FMaterialId\": {" + "                    \"FNumber\": \"01.601010081\"" + // 物料编码
				"                }," + "                \"FUnitID\": {" + "                    \"FNumber\": \"kg\""
				+ "                }," + "                \"FQty\": 1000.0,"
				+ "                \"FPrice\": 442.4778761062," + // 单价
				"                \"FTaxPrice\": 500.0," + // 含税单价
				"                \"FIsFree\": false," + // 是否赠品
				"                \"FEntryTaxRate\": 13.00," + // 税率%
				"                \"FDeliveryDate\": \"2019-06-20 10:48:51\"," + "                \"FStockOrgId\": {" + // 库存组织
				"                    \"FNumber\": \"01\"" + "                },"
				+ "                \"FSettleOrgIds\": {" + // 结算组织
				"                    \"FNumber\": \"01\"" + "                }," + "                \"FSupplyOrgId\": {"
				+ // 供应组织
				"                    \"FNumber\": \"01\"" + "                },"
				+ "                \"FOwnerTypeId\": \"BD_OwnerOrg\"," + // 货主类型
				"                \"FOwnerId\": {" + // 货主
				"                    \"FNumber\": \"01\"" + "                },"
				+ "                \"FReserveType\": \"1\"," + // 预留类型
				"                \"FPriceBaseQty\": 1000.0," + // 计价基本数量
				"                \"FStockUnitID\": {" + // 库存单位
				"                    \"FNumber\": \"kg\"" + "                },"
				+ "                \"FStockQty\": 1000.0," + // 库存数量
				"                \"FStockBaseQty\": 1000.0," + // 库存基本数量
				"                \"FOUTLMTUNIT\": \"SAL\"," + // 超发控制单位类型
				"                \"FOutLmtUnitID\": {" + // 超发控制单位
				"                    \"FNumber\": \"kg\"" + "                }," + "                \"FISMRP\": false,"
				+ // 已计划运算
				"                \"F_ABCD_wl\": \"HIPS 1180(陶氏化学)\"," + // 物料说明
				"                \"FSaleOrderEntry_Link\": [" + "                      {"
				+ "                         \"FSaleOrderEntry_Link_FRuleId\":\"bf475358-d14e-4886-bba1-eedbd220ecd2\","
				+ "						  \"FSaleOrderEntry_Link_FSTableName\":\"T_PUR_POOrderEntry\","
				+ "						  \"FSaleOrderEntry_Link_FSBillId\":" + jrr.getJSONArray(0).getIntValue(0) + "," + // 源单内码
				"						  \"FSaleOrderEntry_Link_FSId\": " + jrr.getJSONArray(0).getIntValue(1) + "" + // 源单分录内码
				"                      }" + "                ]," + // 源单管理
				"                \"FSrcBillNo\": \"" + jrr.getJSONArray(0).getString(3) + "\"," + // 物料说明
				"                \"FSrcType\": \"PUR_PurchaseOrder\"," + // 物料说明
				"                \"FOrderEntryPlan\": [" + // 交货明细
				"                    {" + "                        \"FPlanDate\": \"2019-06-20 10:48:51\","
				+ "                        \"FPlanQty\": 1000.0" + "                    }" + "                ]"
				+ "            }" + "        ]," + "        \"FSaleOrderPlan\": [" + // 收款计划
				"            {" + "                \"FNeedRecAdvance\": false," + // 是否预收
				"                \"FRecAdvanceRate\": 100.0000000000," + // 应收比例(%)
				"                \"FRecAdvanceAmount\": 500000.00" + // 应收金额
				"            }" + "        ]" + "    }" + "}";

		System.out.println(sContentss);

		System.out.println();
		System.out.println();
		System.out.println();

		JSONObject json = new JSONObject();
		json.put("FormId", "SAL_SaleOrder");
		json.put("data", JSONObject.parse(sContentss));

		String ss = client.saveForms(sFormId, json.toString());
		System.out.println(ss);

		// 反写

		// System.out.println (cliient.selectBitchForms(data));
		//
		// cliient.selectBitchForms(data);

		// cliient.deleteParams(j.toString());
		//
		// K3CloudApiClient client = new
		// K3CloudApiClient("http://60.168.155.128:9898/K3cloud/");
		// // var loginResult =
		// client.ValidateLogin("5c91d28df7176a","Administrator","888888",2052);
		// // var resultType =
		// JObject.Parse(loginResult)["LoginResultType"].Value<int>();
		// System.out.println(client.login("5c91d28df7176a","王思亮","888888",2052));
		// System.out.println(client.executeBillQuery(data).size());
		//
	}

	/**
	 * erp保存表单
	 * 
	 * @param
	 */

	public String saveForms(String sFormId, String data) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {

			post.setEntity(new StringEntity(data.toString(), "UTF-8"));
			// post.addHeader("Content-type", "application/json;charset=UTF-8");

			// JSONObject json = new JSONObject();
			// json.put("format", 1);
			// json.put("timestamp", new Date().toString());
			// json.put("v", "1.0");
			// json.put("parameters", data);
			// post.setEntity(new StringEntity(data, "UTF-8"));
			// //post.addHeader("Content-type",
			// "application/json;charset=UTF-8");

			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
			return message;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}
	
	
	/**
	 * erp批量保存表单
	 * 
	 * @param sFormId;data
	 */

	public String saveBatchForms(String sFormId, String data) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.BatchSave.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {

			post.setEntity(new StringEntity(data.toString(), "UTF-8"));
			// post.addHeader("Content-type", "application/json;charset=UTF-8");

			// JSONObject json = new JSONObject();
			// json.put("format", 1);
			// json.put("timestamp", new Date().toString());
			// json.put("v", "1.0");
			// json.put("parameters", data);
			// post.setEntity(new StringEntity(data, "UTF-8"));
			// //post.addHeader("Content-type",
			// "application/json;charset=UTF-8");

			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
			return message;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}
	
	
	
	/**
	 * 客户分配
	 * 
	 * @param
	 */

	public String AllocateForms(String sFormId, String data) {

		HttpPost post = new HttpPost(
				"http://60.168.155.128:9898/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Allocate.common.kdsvc");
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {

			post.setEntity(new StringEntity(data.toString(), "UTF-8"));
			// post.addHeader("Content-type", "application/json;charset=UTF-8");

			// JSONObject json = new JSONObject();
			// json.put("format", 1);
			// json.put("timestamp", new Date().toString());
			// json.put("v", "1.0");
			// json.put("parameters", data);
			// post.setEntity(new StringEntity(data, "UTF-8"));
			// //post.addHeader("Content-type",
			// "application/json;charset=UTF-8");

			response = erpHttp.execute(post, erpContext);
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity);
			return message;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}

}
