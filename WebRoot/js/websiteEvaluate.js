var jsonobj, xAxis1, name1, name2, name3, length1;
// ����echart
require.config({
    paths: {
        echarts: './lib/echarts-2.2.7/build/dist'
    }
});

$("#btn_websiteEvaluate").click(
    function () {
        var date_start = document.getElementById('date1').value;
        var date_end = document.getElementById('date2').value;
        /** **************** ��֤���������� ************************ */
        if (date_start != undefined && date_end != undefined
            && date_start != "" && date_end != "") {
          if(date_start<=date_end){
            $.ajax({
                url: "./websiteEvaluate.json",
                type: "POST",
                data: {
                    "startTime": date_start,
                    "endTime": date_end,
                    "userId": '1',
                    "method": 'getWebTend'
                },
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    status = data.status;
                    message = data.message;
                    if (status == 0) {
                        // ����ɹ�����������
                        jsonobj = data.results;
                        // �Է�����������һ������
                        xAxis1 = jsonobj.xAxis;
                        length1 = jsonobj.xAxis.length;
                        name1 = jsonobj.series[0].name;
                        name2 = jsonobj.series[1].name;
                        name3 = jsonobj.series[2].name;
                        console.log(name1);
                        // ����ɹ������ȴ�ͼ
                        loadEchartBar(jsonobj)
                    } else {
                        // ��ӡ������Ϣ
                        console.log(message);
                    }
                },
        error : function() {
          alert('�������ɹ���');
        }
      });
      }else{
        alert('��ʼ����ӦС�ڽ������ڣ�');
      }
    }else
    {
      alert("����ȷ��д���ڣ�")
    }
    
    });
/*
 * // ·������ require.config({ paths : { echarts :
 * './lib/echarts-2.2.7/build/dist' } });
 */
function loadEchartBar(jsonobj) {
    require(// ��һ��
        ['echarts', 'echarts/chart/bar' // ʹ����״ͼ�ͼ���barģ�飬�������
        ], function (ec) {// ��һ��
            // ����׼���õ�dom����ʼ��echartsͼ��
            var myChart = ec.init(document.getElementById('main1'));

            var option = {
                grid: {
                    x: 30,
                    y: 150,
                    x2: 5,
                    y2: 30
                },
                title: {
                    text: '�ϻ���ѧ��������ֲ�',
                    subtext: '��ý����ϻ���ѧ���ű����У��������۵İ������'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: (function () {
                        // var now = new Date();
                        var res = [];
                        res[0] = name1;
                        res[1] = name2;
                        res[2] = name3;
                        /*
                         * for (var i = 0; i < 3; i++) { res[i] =
                         * jsonobj.series[i].name; }
                         */
                        return res;
                    })()
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {
                            show: true
                        },
                        dataView: {
                            show: true,
                            readOnly: false
                        },
                        magicType: {
                            show: true,
                            type: ['line', 'bar']
                        },
                        restore: {
                            show: true
                        },
                        saveAsImage: {
                            show: true
                        }
                    }
                },
                calculable: true,
                yAxis: [{
                    type: 'value'
                }],
                xAxis: [{
                    type: 'category',
                    data: (function () {
                        // var length1 = xAxis1.length;
                        // var now = new Date();
                        var res = [];
                        for (var i = 0; i < length1; i++) {
                            res[i] = xAxis1[i];
                            console.log(res[i]);
                        }
                        return res;
                    })()
                }],

                series: [{
                    name: name1,
                    type: 'bar',
                    data: (function () {
                        // var length1 = jsonobj.xAxis.length;
                        // var now = new Date();
                        var res = [];
                        for (var i = 0; i < length1; i++) {
                            res[i] = jsonobj.series[0].data[i];
                            // console.log(res[i]);
                        }
                        return res;
                    })()
                }, {
                    name: name2,
                    type: 'bar',
                    data: (function () {
                        // var length1 = jsonobj.xAxis.length;
                        // var now = new Date();
                        var res = [];
                        for (var i = 0; i < length1; i++) {
                            res[i] = jsonobj.series[1].data[i];
                        }
                        return res;
                    })()
                }, {
                    name: name3,
                    type: 'bar',
                    data: (function () {
                        // var length1 = jsonobj.xAxis.length;
                        // var now = new Date();
                        var res = [];
                        for (var i = 0; i < length1; i++) {
                            res[i] = jsonobj.series[2].data[i];
                        }
                        return res;
                    })()
                }]
            };
            // Ϊecharts�����������
            myChart.setOption(option);
            // });
        });
}

