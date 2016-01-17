var jsonobj, status, message;
// ����echart
require.config({
    paths : {
        echarts : './lib/echarts-2.2.7/build/dist'
    }
});

$("#btn_hotword").click(
        function() {
            var date_start = document.getElementById('date1').value;
            var date_end = document.getElementById('date2').value;
            /** **************** ��֤���������� ************************ */
            if (date_start != undefined && date_end != undefined
                    && date_start != "" && date_end != "") {
                if (date_start<=date_end){
                $.ajax({
                    //url : "./servlet/HotWordsServlet",
                    url : "./testword.json",
                    type : "POST",
                    data : {
                        // "startTime":date_start,
                        // "endTime":date_end,
                        "startTime" : '2012-09-10',
                        "endTime" : '2013-01-10',
                        // Ĭ���ȸ�1��������Ҫ�Զ����
                        "userId" : '1',
                        "method" : 'getHotWords'
                    },
                    dataType : "json",
                    success : function(data) {
                        console.log(JSON.stringify(data));
                        status = data.status;
                        message = data.message;
                        if (status == 0) {
                            // ����ɹ�����������
                            jsonobj = data.results;
                            // �Է�����������һ������

                            // ����ɹ������ȴ�ͼ
                            loadEchartForce(jsonobj)
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
                alert('��ʼʱ��ӦС�ڽ���ʱ�䣡');
            }
            } else {
                alert("����ȷ��д���ڣ�")
            }

        });

// ������ݣ�����forceͼ
function loadEchartForce(jsonobj) {
    require(
    // ��һ��
    [ 'echarts', 'echarts/chart/force' // ʹ����״ͼ�ͼ���barģ�飬�������
    ],

            function(ec) {// ��һ��
                // ����׼���õ�dom����ʼ��echartsͼ��
                var myChart = ec.init(document.getElementById('main'));
                var option = {// ��һ��
                    title : {
                        text : '�ȴ�ͳ��',
                        x : 'right',
                        y : 'bottom'
                    },
                    tooltip : {
                        trigger : 'item',
                        formatter : '{a} : {b}'
                    },
                    toolbox : {
                        show : true,
                        feature : {
                            restore : {
                                show : true
                            },
                            magicType : {
                                show : true,
                                type : [ 'force', 'chord' ]
                            },
                            saveAsImage : {
                                show : true
                            }
                        }
                    },
                    legend : {
                        x : 'left',
                        data : [ '��', '��', '��' ]
                    },
                    series : [ {
                        type : 'force',
                        name : "�ȴ�ͳ��",
                        ribbonType : false,
                        categories : [ {
                            name : '��'
                        }, {
                            name : '��'
                        }, {
                            name : '��'
                        } ],
                        itemStyle : {
                            normal : {
                                label : {
                                    show : true,
                                    textStyle : {
                                        color : '#333'
                                    }
                                },
                                nodeStyle : {
                                    brushType : 'both',
                                    borderColor : 'rgba(255,215,0,0.4)',
                                    borderWidth : 1
                                },
                                linkStyle : {
                                    type : 'line'
                                //color:'#5182ab'
                                }
                            },
                            emphasis : {
                                label : {
                                    show : false
                                // textStyle: null // Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                                },
                                nodeStyle : {
                                // r: 30
                                },
                                linkStyle : {}
                            }
                        },
                        useWorker : false,
                        //minRadius : 15,
                        //maxRadius : 25,
                        gravity : 1.1,
                        scaling : 1.1,
                        roam : 'move',

                        nodes : (function() {
                            var length1 = jsonobj.nodes.length;
                            // var now = new Date();
                            function fun(category, name, value, label, index) {
                                this.category = category;
                                this.name = name;
                                this.value = value;
                                this.index = index;
                                if (label == 1)
                                    this.label = this.name + "\n" + "���д�";
                            }

                            // var res = new Object();
                            var res = [];
                            for (var i = 0; i < length1; i++) {
                                // console.log(jsonobj.nodes[i].category);
                                res[i] = new fun(jsonobj.nodes[i].category,
                                        jsonobj.nodes[i].name,
                                        jsonobj.nodes[i].value,
                                        jsonobj.nodes[i].label,
                                        jsonobj.nodes[i].index);

                            }
                            return res;
                        })(),
                        links : (function() {
                            var length1 = jsonobj.links.length;

                            function fun(source, target, weight) {
                                this.source = source;
                                this.target = target;
                                this.weight = weight;

                            }

                            // var res = new Object();
                            var res = [];
                            for (var i = 0; i < length1; i++) {
                                res[i] = new fun(jsonobj.links[i].source,
                                        jsonobj.links[i].target,
                                        jsonobj.links[i].weight);
                                // console.log(res[i]);
                            }
                            return res;
                        })()
                    } ]
                };
                // Ϊecharts�����������
                myChart.setOption(option);
            });
}

