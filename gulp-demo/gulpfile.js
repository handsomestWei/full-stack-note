// 压缩指定目录下的所有js文件并输出

// 1、获取gulp，require()是node中获取模块的语法
var gulp = require('gulp')

// 2、获取uglify模块，用于压缩js。需要安装cnpm install gulp-uglify
var uglify = require('gulp-uglify')

// 压缩js文件。在命令行使用gulp script启动此任务
gulp.task('script', function() {
    // 找到文件
    gulp.src('js/*.js')
    // 压缩文件
        .pipe(uglify())
    // 另存压缩后的文件
        .pipe(gulp.dest('dist/js'))
})

// 在命令行使用gulp auto启动此任务
gulp.task('auto', function () {
    // 监听文件修改，当文件被修改则执行script任务
    gulp.watch('js/*.js', ['script'])
})

// 使用 gulp.task('default') 定义默认任务
// 在命令行使用gulp启动script任务和auto任务
gulp.task('default', ['script', 'auto']);