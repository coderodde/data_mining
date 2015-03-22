var old_grade_pairs =
[[4, 4],
 [4, 5],
 [5, 5],
 [0, 3],
 [1, 5],
 [5, 0],
 [5, 4],
 [5, 5],
 [5, 5],
 [5, 1],
 [4, 1],
 [2, 3],
 [3, 4],
 [4, 4],
 [5, 5],
 [5, 4],
 [5, 0],
 [5, 5],
 [0, 1],
 [2, 2],
 [0, 0],
 [5, 1],
 [3, 0],
 [4, 3],
 [5, 4],
 [1, 0],
 [4, 5],
 [4, 1],
 [3, 2],
 [5, 5],
 [3, 5],
 [5, 3],
 [4, 0],
 [3, 0],
 [1, 2],
 [5, 4],
 [5, 4],
 [5, 5],
 [2, 1],
 [5, 2],
 [2, 1],
 [5, 0],
 [5, 5],
 [2, 2],
 [4, 5],
 [1, 2],
 [2, 3],
 [3, 5],
 [3, 2],
 [3, 5],
 [5, 4],
 [4, 1],
 [3, 5],
 [2, 1],
 [2, 0],
 [0, 0],
 [0, 0],
 [3, 4],
 [3, 0],
 [4, 3],
 [5, 2],
 [3, 5],
 [4, 1],
 [1, 1],
 [5, 0],
 [1, 0],
 [1, 2],
 [0, 3],
 [5, 0],
 [5, 0],
 [5, 3],
 [5, 0],
 [4, 4],
 [5, 5],
 [2, 1]];
 
var new_grade_pairs = 
[[5, 1],
 [4, 4],
 [4, 5],
 [5, 4],
 [5, 5],
 [2, 2],
 [3, 0],
 [5, 4],
 [4, 1],
 [3, 4],
 [3, 0],
 [4, 3],
 [5, 3],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 4],
 [4, 4],
 [5, 1],
 [5, 5],
 [4, 5],
 [1, 1],
 [5, 4],
 [5, 5],
 [3, 2],
 [3, 2],
 [4, 5],
 [5, 3],
 [5, 4],
 [2, 0],
 [3, 0],
 [5, 5],
 [5, 5],
 [4, 5],
 [2, 3],
 [5, 4],
 [4, 2],
 [5, 3],
 [5, 2],
 [3, 5],
 [4, 5],
 [2, 4],
 [5, 3],
 [5, 3],
 [5, 5],
 [5, 5],
 [5, 4],
 [5, 4],
 [5, 5],
 [3, 5],
 [5, 5],
 [4, 5],
 [3, 3],
 [5, 4],
 [0, 3],
 [5, 2],
 [5, 3],
 [5, 4],
 [2, 3],
 [4, 5],
 [2, 1],
 [4, 2],
 [4, 5],
 [4, 4],
 [4, 4],
 [5, 5],
 [5, 4],
 [4, 5],
 [5, 5],
 [5, 5],
 [5, 5],
 [4, 3],
 [5, 5],
 [4, 4],
 [4, 5],
 [5, 5],
 [5, 5],
 [5, 1],
 [5, 5],
 [4, 5],
 [5, 4],
 [4, 1],
 [5, 3],
 [4, 5],
 [4, 3],
 [5, 4],
 [3, 5],
 [5, 4],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 4],
 [5, 5],
 [5, 5],
 [4, 4],
 [5, 5],
 [5, 4],
 [4, 4],
 [5, 5],
 [5, 4],
 [5, 5],
 [5, 5],
 [4, 5],
 [5, 5],
 [5, 5],
 [5, 0],
 [1, 0],
 [5, 3],
 [5, 3],
 [5, 5],
 [5, 5],
 [2, 2],
 [5, 3],
 [5, 5],
 [5, 4],
 [5, 5],
 [4, 5],
 [5, 5],
 [5, 5],
 [4, 5],
 [0, 0],
 [5, 0],
 [2, 2],
 [5, 5],
 [5, 4],
 [5, 1],
 [5, 4],
 [4, 5],
 [5, 4],
 [4, 3],
 [4, 5],
 [4, 2],
 [4, 3],
 [5, 1],
 [4, 4],
 [4, 5],
 [5, 0],
 [4, 4],
 [5, 5],
 [1, 1],
 [5, 1],
 [4, 3],
 [5, 4],
 [2, 1],
 [5, 5],
 [5, 5],
 [2, 3],
 [3, 0],
 [5, 3],
 [3, 2],
 [4, 1],
 [1, 4],
 [5, 2],
 [5, 2],
 [3, 2],
 [0, 0],
 [3, 2],
 [1, 4],
 [2, 5],
 [4, 5],
 [5, 2],
 [3, 5],
 [5, 4],
 [4, 4],
 [5, 5],
 [3, 5],
 [3, 4],
 [4, 4],
 [5, 4],
 [1, 1],
 [5, 4],
 [5, 1],
 [5, 3],
 [3, 3],
 [1, 4],
 [5, 3],
 [4, 5],
 [4, 5],
 [2, 4],
 [4, 1],
 [5, 5],
 [5, 4],
 [5, 3],
 [4, 3],
 [5, 5],
 [5, 5],
 [5, 5],
 [4, 4],
 [5, 5],
 [5, 5],
 [5, 4],
 [5, 5],
 [5, 4],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 5],
 [4, 4],
 [5, 5],
 [4, 5],
 [4, 5],
 [5, 5],
 [5, 2],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 5],
 [4, 4],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 4],
 [5, 4],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 4],
 [2, 1],
 [5, 5],
 [5, 5],
 [4, 5],
 [5, 5],
 [5, 5],
 [4, 5],
 [5, 4],
 [5, 4],
 [5, 5],
 [4, 5],
 [3, 5],
 [5, 5],
 [4, 2],
 [0, 1],
 [4, 2],
 [3, 4],
 [3, 3],
 [5, 4],
 [5, 4],
 [5, 5],
 [5, 4],
 [5, 2],
 [5, 5],
 [4, 4],
 [5, 5],
 [4, 4],
 [4, 5],
 [3, 2],
 [5, 4],
 [5, 4],
 [4, 5],
 [3, 2],
 [2, 1],
 [4, 5],
 [5, 4],
 [0, 4],
 [2, 0],
 [5, 5],
 [5, 4],
 [1, 3],
 [2, 0],
 [3, 3],
 [4, 2],
 [4, 4],
 [4, 3],
 [4, 5],
 [5, 5],
 [2, 4],
 [2, 1],
 [5, 3],
 [5, 3],
 [3, 5],
 [5, 5],
 [3, 1],
 [5, 4],
 [4, 3],
 [5, 5],
 [4, 4],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 5],
 [5, 4],
 [4, 4],
 [3, 4],
 [5, 4],
 [5, 0],
 [4, 5],
 [5, 4],
 [3, 2],
 [5, 3],
 [5, 5],
 [4, 5],
 [5, 5],
 [5, 5],
 [4, 3],
 [4, 5],
 [5, 5],
 [5, 4],
 [5, 1],
 [2, 2],
 [3, 4],
 [5, 5],
 [5, 4],
 [4, 3],
 [5, 3],
 [5, 5],
 [4, 5],
 [5, 5],
 [3, 3],
 [5, 4],
 [4, 3],
 [5, 3],
 [4, 4],
 [4, 3],
 [5, 0],
 [4, 5],
 [5, 3],
 [5, 4],
 [4, 4],
 [5, 4],
 [5, 4],
 [2, 4],
 [5, 5],
 [4, 4],
 [4, 2],
 [5, 5],
 [1, 2],
 [5, 3],
 [3, 4],
 [5, 4],
 [2, 5],
 [5, 1],
 [4, 4],
 [5, 5],
 [1, 5],
 [5, 3],
 [5, 4],
 [2, 4],
 [5, 5],
 [4, 2],
 [4, 3],
 [5, 5],
 [4, 4],
 [1, 2],
 [5, 4],
 [5, 5],
 [1, 2],
 [5, 4],
 [3, 5],
 [5, 5],
 [4, 4],
 [5, 5],
 [5, 4],
 [5, 5],
 [5, 5],
 [4, 5],
 [5, 2],
 [4, 0],
 [4, 4],
 [2, 2],
 [4, 1],
 [5, 5],
 [4, 0],
 [5, 5],
 [4, 2],
 [2, 1],
 [3, 0]];        

function init_matrix(matrix) {
    for (var y = 0; y <= 5; ++y) {
        matrix[y] = new Array(6);
        for (var x = 0; x <= 5; ++x) {
            matrix[y][x] = 0;
        }
    }
}

function set_matrix(matrix, grade_pairs) {
    for (var i = 0; i < grade_pairs.length; ++i) {
        matrix[grade_pairs[i][1]][grade_pairs[i][0]]++;
    }
}

// if weight is 0, return the color string representing white,
// if weight is 1, return the color string representing blue.
function get_color_string(weight) {
    weight = 1 - weight;
    var red = Math.round(255 * weight).toString(16);
    var green = Math.round(255 * weight).toString(16);
    return "#" + red + green + "ff";
}

function get_maximum_cell(matrix) {
    var max = 0;
    
    for (var y = 0; y < matrix.length; ++y) {
        for (var x = 0; x < matrix[y].length; ++x) {
            if (max < matrix[y][x]) {
                max = matrix[y][x];
            }
        }
    }
    
    return max;
}

function normalize(matrix) {
    var max = get_maximum_cell(matrix);
    
    for (var y = 0; y < matrix.length; ++y) {
        for (var x = 0; x < matrix[y].length; ++x) {
            matrix[y][x] /= max;
        }
    }
}

function draw_matrix(matrix, canvas) {
    var ctx = canvas.getContext("2d");
    ctx.fillStyle = "#000000";
    ctx.font = "20px Arial";
    
    // Draw vertical scale.
    for (var i = 0; i <= 5; ++i) {
        ctx.fillText(5 - i, 0, 60 * (i + 1));
    }
    
    // Draw horizontal scale.
    for (var i = 0; i <= 5; ++i) {
        ctx.fillText(i, 60 * i + 40, 400);
    }
    
    // Draw the actual visualization.
    for (var y = 0; y <= 5; ++y) {
        for (var x = 0; x <= 5; ++x) {
            ctx.fillStyle = get_color_string(matrix[y][x]);
            ctx.fillRect(40 + x * 60, (5 - y) * 60, 60, 60);
        }
    }
}

function mean_x(pairs) {
    var sum = 0;
    
    for (var i = 0; i < pairs.length; ++i) {
        sum += pairs[i][0];
    }
    
    return sum / pairs.length;
}

function mean_y(pairs) {
    var sum = 0;
    
    for (var i = 0; i < pairs.length; ++i) {
        sum += pairs[i][1];
    }
    
    return sum / pairs.length;
}

function correlation(pairs) {
    var upper = 0;
    var meanx = mean_x(pairs);
    var meany = mean_y(pairs);
    
    for (var i = 0; i < pairs.length; ++i) {
        upper += (pairs[i][0] - meanx) * (pairs[i][1] - meany);
    }
    
    var lowera = 0;
    var lowerb = 0;
    
    for (var i = 0; i < pairs.length; ++i) {
        lowera += (pairs[i][0] - meanx) * (pairs[i][0] - meanx);
        lowerb += (pairs[i][1] - meany) * (pairs[i][1] - meany);
    }
    
    return upper / (Math.sqrt(lowera) * Math.sqrt(lowerb));
}

var old_canvas = document.getElementById("old_canvas");
var new_canvas = document.getElementById("new_canvas");

old_canvas.width = 400;
old_canvas.height = 400;

new_canvas.width = 400;
new_canvas.height = 400;

var old_matrix = [];
var new_matrix = [];

init_matrix(old_matrix);
init_matrix(new_matrix);

set_matrix(old_matrix, old_grade_pairs);
set_matrix(new_matrix, new_grade_pairs);

normalize(old_matrix);
normalize(new_matrix);

draw_matrix(old_matrix, old_canvas);
draw_matrix(new_matrix, new_canvas);

alert("my.js is here!");
alert("Old correlation: " + correlation(old_grade_pairs));
alert("New correlation: " + correlation(new_grade_pairs));

