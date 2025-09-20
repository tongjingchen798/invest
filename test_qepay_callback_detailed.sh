#!/bin/bash

# QePay代付回调详细测试脚本
# 模拟QePay的8次重复通知机制

# 配置
CALLBACK_URL="http://localhost:8082/api/qepay/payout/notify"
SECRET_KEY="test_secret_key"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 生成测试签名（模拟）
generate_signature() {
    local params="$1"
    local key="$2"
    
    # 这里应该调用Java程序生成真实的签名
    # 为了测试，我们使用模拟签名
    echo "mock_signature_$(date +%s)"
}

# 测试代付成功回调（模拟8次重复通知）
test_payout_success_repeated() {
    echo -e "${BLUE}=== 测试QePay代付成功回调（模拟8次重复通知）===${NC}"
    
    # 构建参数
    local tradeResult="1"
    local merTransferId="QEPAY_SUCCESS_$(date +%s)"
    local merNo="QEPAY123456"
    local tradeNo="3000025"
    local transferAmount="100.00"
    local applyDate="2024-01-01 11:33:59"
    local version="1.0"
    local respCode="SUCCESS"
    
    # 构建签名参数（不包含sign和signType）
    local signParams="applyDate=${applyDate}&merNo=${merNo}&merTransferId=${merTransferId}&respCode=${respCode}&tradeNo=${tradeNo}&tradeResult=${tradeResult}&transferAmount=${transferAmount}&version=${version}"
    
    # 生成签名
    local signature=$(generate_signature "$signParams" "$SECRET_KEY")
    
    # 构建完整请求数据
    local formData="${signParams}&sign=${signature}&signType=MD5"
    
    echo -e "${YELLOW}订单号: ${merTransferId}${NC}"
    echo -e "${YELLOW}请求URL: ${CALLBACK_URL}${NC}"
    echo -e "${YELLOW}请求数据: ${formData}${NC}"
    echo ""
    
    # 模拟QePay的8次重复通知
    for i in {1..8}; do
        echo -e "${BLUE}--- 第 ${i} 次通知 ---${NC}"
        
        # 发送请求
        response=$(curl -s -X POST "$CALLBACK_URL" \
            -H "Content-Type: application/x-www-form-urlencoded" \
            -d "$formData")
        
        echo -e "响应: ${response}"
        
        # 如果收到success，说明处理成功，平台应该停止发送
        if [ "$response" = "success" ]; then
            echo -e "${GREEN}✓ 第 ${i} 次通知收到success响应，平台将停止发送后续通知${NC}"
            break
        else
            echo -e "${RED}✗ 第 ${i} 次通知收到fail响应${NC}"
        fi
        
        # 模拟通知间隔（实际QePay可能有固定间隔）
        sleep 1
    done
    
    echo -e "${BLUE}=== 代付成功重复通知测试完成 ===${NC}"
    echo ""
}

# 测试代付失败回调
test_payout_failure() {
    echo -e "${BLUE}=== 测试QePay代付失败回调 ===${NC}"
    
    # 构建参数
    local tradeResult="2"
    local merTransferId="QEPAY_FAIL_$(date +%s)"
    local merNo="QEPAY123456"
    local tradeNo="3000026"
    local transferAmount="50.00"
    local applyDate="2024-01-01 11:35:00"
    local version="1.0"
    local respCode="SUCCESS"
    
    # 构建签名参数
    local signParams="applyDate=${applyDate}&merNo=${merNo}&merTransferId=${merTransferId}&respCode=${respCode}&tradeNo=${tradeNo}&tradeResult=${tradeResult}&transferAmount=${transferAmount}&version=${version}"
    
    # 生成签名
    local signature=$(generate_signature "$signParams" "$SECRET_KEY")
    
    # 构建完整请求数据
    local formData="${signParams}&sign=${signature}&signType=MD5"
    
    echo -e "${YELLOW}订单号: ${merTransferId}${NC}"
    echo -e "${YELLOW}请求URL: ${CALLBACK_URL}${NC}"
    echo -e "${YELLOW}请求数据: ${formData}${NC}"
    echo ""
    
    # 发送请求
    response=$(curl -s -X POST "$CALLBACK_URL" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "$formData")
    
    echo -e "响应: ${response}"
    
    if [ "$response" = "success" ]; then
        echo -e "${GREEN}✓ 代付失败回调处理成功${NC}"
    else
        echo -e "${RED}✗ 代付失败回调处理失败${NC}"
    fi
    
    echo -e "${BLUE}=== 代付失败回调测试完成 ===${NC}"
    echo ""
}

# 测试无效签名
test_invalid_signature() {
    echo -e "${BLUE}=== 测试无效签名 ===${NC}"
    
    # 构建参数
    local tradeResult="1"
    local merTransferId="QEPAY_INVALID_$(date +%s)"
    local merNo="QEPAY123456"
    local tradeNo="3000027"
    local transferAmount="200.00"
    local applyDate="2024-01-01 11:40:00"
    local version="1.0"
    local respCode="SUCCESS"
    local signature="invalid_signature"
    
    # 构建请求数据
    local formData="applyDate=${applyDate}&merNo=${merNo}&merTransferId=${merTransferId}&respCode=${respCode}&sign=${signature}&signType=MD5&tradeNo=${tradeNo}&tradeResult=${tradeResult}&transferAmount=${transferAmount}&version=${version}"
    
    echo -e "${YELLOW}订单号: ${merTransferId}${NC}"
    echo -e "${YELLOW}请求URL: ${CALLBACK_URL}${NC}"
    echo -e "${YELLOW}请求数据: ${formData}${NC}"
    echo ""
    
    # 发送请求
    response=$(curl -s -X POST "$CALLBACK_URL" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "$formData")
    
    echo -e "响应: ${response}"
    
    if [ "$response" = "fail" ]; then
        echo -e "${GREEN}✓ 无效签名正确返回fail${NC}"
    else
        echo -e "${RED}✗ 无效签名应该返回fail${NC}"
    fi
    
    echo -e "${BLUE}=== 无效签名测试完成 ===${NC}"
    echo ""
}

# 测试重复处理（相同订单号多次回调）
test_duplicate_processing() {
    echo -e "${BLUE}=== 测试重复处理（相同订单号多次回调）===${NC}"
    
    # 构建参数
    local tradeResult="1"
    local merTransferId="QEPAY_DUPLICATE_$(date +%s)"
    local merNo="QEPAY123456"
    local tradeNo="3000028"
    local transferAmount="300.00"
    local applyDate="2024-01-01 11:45:00"
    local version="1.0"
    local respCode="SUCCESS"
    
    # 构建签名参数
    local signParams="applyDate=${applyDate}&merNo=${merNo}&merTransferId=${merTransferId}&respCode=${respCode}&tradeNo=${tradeNo}&tradeResult=${tradeResult}&transferAmount=${transferAmount}&version=${version}"
    
    # 生成签名
    local signature=$(generate_signature "$signParams" "$SECRET_KEY")
    
    # 构建完整请求数据
    local formData="${signParams}&sign=${signature}&signType=MD5"
    
    echo -e "${YELLOW}订单号: ${merTransferId}${NC}"
    echo -e "${YELLOW}请求URL: ${CALLBACK_URL}${NC}"
    echo -e "${YELLOW}请求数据: ${formData}${NC}"
    echo ""
    
    # 发送多次相同请求
    for i in {1..3}; do
        echo -e "${BLUE}--- 第 ${i} 次相同订单回调 ---${NC}"
        
        # 发送请求
        response=$(curl -s -X POST "$CALLBACK_URL" \
            -H "Content-Type: application/x-www-form-urlencoded" \
            -d "$formData")
        
        echo -e "响应: ${response}"
        
        if [ "$response" = "success" ]; then
            echo -e "${GREEN}✓ 第 ${i} 次回调处理成功${NC}"
        else
            echo -e "${RED}✗ 第 ${i} 次回调处理失败${NC}"
        fi
        
        sleep 1
    done
    
    echo -e "${BLUE}=== 重复处理测试完成 ===${NC}"
    echo ""
}

# 显示测试说明
show_test_info() {
    echo -e "${GREEN}QePay代付回调详细测试脚本${NC}"
    echo -e "${YELLOW}回调URL: ${CALLBACK_URL}${NC}"
    echo -e "${YELLOW}密钥: ${SECRET_KEY}${NC}"
    echo ""
    echo -e "${BLUE}测试内容:${NC}"
    echo "1. 代付成功回调（模拟8次重复通知）"
    echo "2. 代付失败回调"
    echo "3. 无效签名测试"
    echo "4. 重复处理测试（相同订单号多次回调）"
    echo ""
}

# 主函数
main() {
    show_test_info
    
    # 检查curl是否可用
    if ! command -v curl &> /dev/null; then
        echo -e "${RED}错误: curl命令未找到，请先安装curl${NC}"
        exit 1
    fi
    
    # 运行测试
    test_payout_success_repeated
    test_payout_failure
    test_invalid_signature
    test_duplicate_processing
    
    echo -e "${GREEN}所有测试完成！${NC}"
    echo ""
    echo -e "${YELLOW}注意事项:${NC}"
    echo "1. 确保QePayCallbackController已正确部署"
    echo "2. 确保数据库连接正常"
    echo "3. 确保相关DAO和Service已正确配置"
    echo "4. 查看应用日志了解详细处理过程"
}

# 运行主函数
main "$@"
