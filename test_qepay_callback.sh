#!/bin/bash

# QePay代付回调测试脚本
# 用于测试QePay代付回调接口

# 配置
CALLBACK_URL="http://localhost:8082/api/payout/qepay/notify"
SECRET_KEY="test_secret_key"

# 生成测试签名
generate_signature() {
    local params="$1"
    local key="$2"
    
    # 使用Java程序生成签名（需要先编译）
    echo "正在生成签名..."
    # 这里应该调用Java程序生成签名，暂时使用模拟值
    echo "mock_signature_123456"
}

# 测试代付成功回调
test_payout_success() {
    echo "=== 测试代付成功回调 ==="
    
    # 构建参数
    local tradeResult="1"
    local merTransferId="QEPAY_TEST_$(date +%s)"
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
    
    echo "请求URL: $CALLBACK_URL"
    echo "请求数据: $formData"
    echo ""
    
    # 发送请求
    curl -X POST "$CALLBACK_URL" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "$formData" \
        -v
    
    echo ""
    echo "=== 代付成功回调测试完成 ==="
    echo ""
}

# 测试代付失败回调
test_payout_failure() {
    echo "=== 测试代付失败回调 ==="
    
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
    
    echo "请求URL: $CALLBACK_URL"
    echo "请求数据: $formData"
    echo ""
    
    # 发送请求
    curl -X POST "$CALLBACK_URL" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "$formData" \
        -v
    
    echo ""
    echo "=== 代付失败回调测试完成 ==="
    echo ""
}

# 测试无效签名
test_invalid_signature() {
    echo "=== 测试无效签名 ==="
    
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
    
    echo "请求URL: $CALLBACK_URL"
    echo "请求数据: $formData"
    echo ""
    
    # 发送请求
    curl -X POST "$CALLBACK_URL" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "$formData" \
        -v
    
    echo ""
    echo "=== 无效签名测试完成 ==="
    echo ""
}

# 主函数
main() {
    echo "QePay代付回调测试开始..."
    echo "回调URL: $CALLBACK_URL"
    echo "密钥: $SECRET_KEY"
    echo ""
    
    # 检查curl是否可用
    if ! command -v curl &> /dev/null; then
        echo "错误: curl命令未找到，请先安装curl"
        exit 1
    fi
    
    # 运行测试
    test_payout_success
    test_payout_failure
    test_invalid_signature
    
    echo "所有测试完成！"
}

# 运行主函数
main "$@"
