//使用html5框架phaser进行初始化
var game = new Phaser.Game(500, 400, Phaser.AUTO, 'game_div');
var flappyBirdState = {};
//建立主场景
flappyBirdState.main = function() { };
flappyBirdState.main.prototype = {

    //预加载
    preload: function() { 
        //改变背景颜色
        this.game.stage.backgroundColor = '#71c5cf';
        //加载小鸟图片素材
        this.game.load.image('bird', 'assets/birds.png');
        //加载柱子素材
        this.game.load.image('pipe', 'assets/pipe.png');
    },

    //建立游戏的方法
    create: function() { 
        //渲染小鸟
        this.bird = this.game.add.sprite(100, 245, 'bird');
        //给小鸟素材添加y方向上的重力效果
        this.bird.body.gravity.y = 1000;
        //var spaceKey = this.game.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR);
        //spaceKey.onDown.add(this.jump, this);
        //绑定触屏事件
        var t=this;
        this.game.input.touch.onTouchStart=function(){
        t.jump();
        }

        //创建20个管道块
        this.pipes = game.add.group();
        this.pipes.createMultiple(20, 'pipe');  

        //1.5秒调用一次add_row_of_pipes
        this.timer = this.game.time.events.loop(1500, this.add_row_of_pipes, this);           

        //屏幕上添加分数显示
        this.score = 0;
        var style = { font: "30px Arial", fill: "#ffffff" };
        this.labelScore = this.game.add.text(20, 20, "0", style);  
    },

	//更新小鸟状态
	//每秒检查60次
	update: function() {
        //检查小鸟是否死亡
        if (this.bird.inWorld == false)
            this.restartGame(); 
        //小鸟碰到柱子则会死亡
        this.game.physics.overlap(this.bird, this.pipes, this.restartGame, null, this);      
    },
    //小鸟跳跃的方法
    jump: function() {
        //添加垂直方向上的力
        this.bird.body.velocity.y = -350;
    },

    //重置游戏
    restartGame: function() {
        //移除计时器
        this.game.time.events.remove(this.timer);
        //从main状态开始
        this.game.state.start('main');
    },

    //添加管道
    add_one_pipe: function(x, y) {
        var pipe = this.pipes.getFirstDead();

        //设置管道的位置
        pipe.reset(x, y);

         //为管道添加向左移动的速度
        pipe.body.velocity.x = -200; 
               
        //管道不可见的时候删除
        pipe.outOfBoundsKill = true;
    },

    //添加留个块组成的管道以及一个可通过的洞
    add_row_of_pipes: function() {
        var hole = Math.floor(Math.random()*6)+1;
        
        for (var i = 0; i < 7; i++)
            if (i != hole && i != hole +1) 
                this.add_one_pipe(400, i*60+10);   
    
        this.score += 1;
        this.labelScore.content = this.score;  
    },
};

game.state.add('main', flappyBirdState.main);  
game.state.start('main'); 