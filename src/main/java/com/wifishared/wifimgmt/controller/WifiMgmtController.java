package com.wifishared.wifimgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.wifishared.common.data.dto.hotspot.HotSpotReqBody;
import com.wifishared.common.data.dto.hotspot.HotSpotReqParam;
import com.wifishared.common.data.otd.hotspot.HotSpotRspBody;
import com.wifishared.common.framework.resultobj.GeneralContentResult;
import com.wifishared.common.framework.resultobj.GeneralResult;
import com.wifishared.wifimgmt.service.HotSpotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="热点信息")
@RestController
@Slf4j
public class WifiMgmtController {

	@Autowired
	HotSpotService hotSpotService;

	@ApiOperation(value = "创建热点信息")
	@RequestMapping(value = "/auth/v1/hotspots", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralContentResult<String> createHotSpot(
			@RequestBody HotSpotReqBody hotSpotReqBody) {
		log.info("create hotspot body:{}",hotSpotReqBody.toString());
		GeneralContentResult<String> result = hotSpotService
				.createHotSpot(hotSpotReqBody);
		return result;
	}

	@ApiOperation(value = "查询热点信息")
	@RequestMapping(value = "/auth/v1/hotspots/{hotspotId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralContentResult<HotSpotRspBody> findHotSpot(
			@PathVariable("hotspotId") String hotspotId) {
		GeneralContentResult<HotSpotRspBody> result = hotSpotService
				.findHotSpot(hotspotId);
		return result;
	}
	
	@ApiOperation(value = "更新热点信息")
	@RequestMapping(value = "/auth/v1/hotspots/{hotspotId}", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralResult updateHotSpot(
			@PathVariable("hotspotId") String hotspotId,
			@RequestBody HotSpotReqBody hotSpotReqBody) {
		GeneralResult result = hotSpotService
				.updateHotSpot(hotspotId,
						hotSpotReqBody);
		return result;
	}
	
	@ApiOperation(value = "使热点信息失效")
	@RequestMapping(value = "/auth/v1/hotspots/{hotspotId}/disabled", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralResult disableHotSpot(
			@PathVariable("hotspotId") String hotspotId) {
		GeneralResult result = hotSpotService
				.disableHotSpot(hotspotId);
		return result;
	}
	
	@ApiOperation(value = "删除热点信息")
	@RequestMapping(value = "/auth/v1/hotspots/{hotspotId}", method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralResult deleteHotSpot(
			@PathVariable("hotspotId") String hotspotId) {
		GeneralResult result = hotSpotService
				.deleteHotSpot(hotspotId);
		return result;
	}
	
	@ApiOperation(value = "根据一些信息查询热点信息")
	@RequestMapping(value = "/noauth/v1/hotspots/location", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralContentResult<List<HotSpotRspBody>> findHotSpotBySomeAttr(
			@RequestBody HotSpotReqParam param) {
		GeneralContentResult<List<HotSpotRspBody>> result = hotSpotService
				.findHotSpotBySomeAttr(param);
		return result;
	}
	
	
}
