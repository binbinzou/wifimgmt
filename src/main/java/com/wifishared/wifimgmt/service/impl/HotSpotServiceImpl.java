package com.wifishared.wifimgmt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wifishared.common.data.dto.hotspot.HotSpotReqBody;
import com.wifishared.common.data.dto.hotspot.HotSpotReqParam;
import com.wifishared.common.data.dto.hotspot.WifiReqParam;
import com.wifishared.common.data.otd.hotspot.HotSpotRspBody;
import com.wifishared.common.framework.contant.CommonResultCodeConstant;
import com.wifishared.common.framework.contant.CommonResultMessageConstant;
import com.wifishared.common.framework.resultobj.GeneralContentResult;
import com.wifishared.common.framework.resultobj.GeneralResult;
import com.wifishared.common.framework.rsa.RSACoder;
import com.wifishared.wifimgmt.constant.HotSpotConstant;
import com.wifishared.wifimgmt.domain.Hotspotconfig;
import com.wifishared.wifimgmt.repository.HotspotconfigRepository;
import com.wifishared.wifimgmt.service.HotSpotService;
import com.wifishared.wifimgmt.utils.HotspotconfigConverter;

@Service
public class HotSpotServiceImpl implements HotSpotService {

	@Autowired
	HotspotconfigRepository hotspotconfigRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GeneralContentResult<String> createHotSpot(HotSpotReqBody hotSpotReqBody) {
		GeneralContentResult<String> result = new GeneralContentResult<String>();
		Hotspotconfig hotspotconfig = HotspotconfigConverter.hotSpotReqBody2Hotspotconfig(hotSpotReqBody);
		hotspotconfig.setCreator("xxx");
		hotspotconfig.setStatus(HotSpotConstant.HOTSPOT_CONFIG_ENABLE);
		Hotspotconfig hotspot = hotspotconfigRepository.save(hotspotconfig);
		result.setCode(CommonResultCodeConstant.OPERATE_SUCCESS);
		result.setContent(hotspot.getId());
		result.setMessage("热点创建成功");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GeneralResult updateHotSpot(String hotspotId, HotSpotReqBody hotSpotReqBody) {
		GeneralResult result = new GeneralResult();
		Hotspotconfig hotspotconfig = HotspotconfigConverter.hotSpotReqBody2Hotspotconfig(hotSpotReqBody);
		hotspotconfig.setId(hotspotId);
		hotspotconfig.setCreator("xxx");
		hotspotconfig.setStatus(HotSpotConstant.HOTSPOT_CONFIG_ENABLE);
		Hotspotconfig hotspot = hotspotconfigRepository.save(hotspotconfig);
		result.setCode(CommonResultCodeConstant.OPERATE_SUCCESS);
		result.setMessage("热点更新成功");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GeneralResult disableHotSpot(String hotspotId) {
		int tmp = hotspotconfigRepository.setStatusFor(HotSpotConstant.HOTSPOT_CONFIG_DISABLE, hotspotId);
		GeneralResult result = new GeneralResult();
		if (tmp > 0) {
			result.setCode(CommonResultCodeConstant.OPERATE_SUCCESS);
			result.setMessage(CommonResultMessageConstant.OPERATE_SUCCESS);
		} else {
			result.setCode(CommonResultCodeConstant.WIFI_ERROR);
			result.setMessage(CommonResultMessageConstant.UNKNOW_ERROR);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GeneralResult deleteHotSpot(String hotspotId) {
		int tmp = hotspotconfigRepository.setStatusFor(HotSpotConstant.HOTSPOT_CONFIG_DELETE, hotspotId);
		GeneralResult result = new GeneralResult();
		if (tmp > 0) {
			result.setCode(CommonResultCodeConstant.OPERATE_SUCCESS);
			result.setMessage(CommonResultMessageConstant.OPERATE_SUCCESS);
		} else {
			result.setCode(CommonResultCodeConstant.WIFI_ERROR);
			result.setMessage(CommonResultMessageConstant.UNKNOW_ERROR);
		}
		return result;
	}

	@Override
	public GeneralContentResult<List<HotSpotRspBody>> findHotSpotBySomeAttr(HotSpotReqParam param) {
		GeneralContentResult<List<HotSpotRspBody>> result = new GeneralContentResult<List<HotSpotRspBody>>();
		List<HotSpotRspBody> rspBodies = new ArrayList<HotSpotRspBody>();
		double lng = Double.parseDouble(param.getLocationReqParam().getLng());
		double lat = Double.parseDouble(param.getLocationReqParam().getLat());
		double r = 6371;// 地球半径千米
		double dis = 0.5;// 0.5千米距离
		double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(lat * Math.PI / 180));
		dlng = dlng * 180 / Math.PI;// 角度转为弧度
		double dlat = dis / r;
		dlat = dlat * 180 / Math.PI;
		double minlat = lat - dlat;
		double maxlat = lat + dlat;
		double minlng = lng - dlng;
		double maxlng = lng + dlng;
		List<Hotspotconfig> hotspotconfigs = hotspotconfigRepository.findByBssidAnsSsidAndLngAndLat(minlat, maxlat, minlng, maxlng, HotSpotConstant.HOTSPOT_CONFIG_ENABLE);
		Map<String, Object> map = null;
		try {
			map = RSACoder.initKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Hotspotconfig hotspotconfig : hotspotconfigs) {
			for(WifiReqParam reqParamList :param.getWifiReqParam()) {
				if(hotspotconfig.getSsid().equals(reqParamList.getSsid())&&hotspotconfig.getBssid().equals(reqParamList.getBssid())) {
					HotSpotRspBody rspBody = new HotSpotRspBody();
					rspBody = HotspotconfigConverter.hotspotconfig2HotSpotRspBody(hotspotconfig,map);
					rspBodies.add(rspBody);
				}
			}
		}
		result.setCode(CommonResultCodeConstant.OPERATE_SUCCESS);
		result.setMessage(CommonResultMessageConstant.OPERATE_SUCCESS);
		result.setContent(rspBodies);
		return result;
	}

	@Override
	public GeneralContentResult<HotSpotRspBody> findHotSpot(String hotspotId) {
		GeneralContentResult<HotSpotRspBody> result = new GeneralContentResult<HotSpotRspBody>();
		result.setCode(CommonResultCodeConstant.OPERATE_SUCCESS);
		result.setMessage("查询成功");
		Hotspotconfig hotspotconfig = hotspotconfigRepository.getOne(hotspotId);
		Map<String, Object> map = null;
		try {
			map = RSACoder.initKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(hotspotconfig!=null) {
			HotSpotRspBody rspBody = HotspotconfigConverter.hotspotconfig2HotSpotRspBody(hotspotconfig,map);
			result.setContent(rspBody);
		}else {
			result.setContent(null);
		}
		return result;
	}

}
