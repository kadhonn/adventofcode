use std::collections::{HashMap};

const TILES: &str = "####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##";


const ROUNDS: i64 = 1000000000000;

pub fn day17_1(str: &str) {
    let tiles = parse_tiles();
    let wind: Vec<char> = str.trim().chars().collect();

    let mut field = vec![vec![false; 7]; 3000];

    let mut tiles_i: usize = 0;
    let mut wind_i: usize = 0;
    let mut floor: usize = 0;
    let mut cut: i64 = 0;
    let mut poses: HashMap<(usize, usize), (usize, i64)> = HashMap::new();
    let mut rythms = HashMap::new();
    let mut i = 0;
    while i < ROUNDS as i64 {
        let pos = (tiles_i, wind_i);
        if poses.contains_key(&pos) {
            let old_floor = poses.get(&pos).unwrap().0;
            let old_i = poses.get(&pos).unwrap().1;
            let floor_change = floor - old_floor;
            let hertz = i - old_i;
            let rythm_key = (tiles_i, wind_i, hertz);
            if rythms.contains_key(&rythm_key) {
                let old_floor_change = rythms.get(&rythm_key).unwrap();
                if floor_change == *old_floor_change {
                    println!("found! ");
                    let needed = ROUNDS - i;
                    let needed_rounds = needed / hertz;
                    i += needed_rounds * hertz;
                    floor += needed_rounds as usize * floor_change;
                    cut += needed_rounds * floor_change as i64;
                }
            }
            rythms.insert(rythm_key, floor_change);
            // println!("{} - {} = {} {} - {} = {} ", floor, old_floor, floor - old_floor, i, old_i, i - old_i);
        }
        poses.insert(pos, (floor, i));
        let next_tile = &tiles[tiles_i];
        tiles_i = (tiles_i + 1) % tiles.len();
        let height = next_tile.len();

        let mut x: i64 = 2;
        let mut y: i64 = floor as i64 + 2 + height as i64;

        loop {
            let new_x = x as i64 + match wind[wind_i] {
                '<' => -1,
                '>' => 1,
                wat => panic!("invalid wind: {}", wat)
            };
            wind_i = (wind_i + 1) % wind.len();
            if !crashes(&field, cut, next_tile, new_x, y) {
                x = new_x;
            }

            y -= 1;
            if crashes(&field, cut, next_tile, x, y) {
                y += 1;
                apply_tile(&mut field, cut, next_tile, x, y);
                floor = floor.max(y as usize + 1);
                break;
            }
            if cut + 2000 < floor as i64 {
                cut += 1000;
                for _ in 0..1000 {
                    field.remove(0);
                }
                for _ in 0..1000 {
                    field.push(vec![false; 7]);
                }
            }
        }
        i += 1;
        //print_field(&field, floor);
    }

    // print_field(&field, floor);

    println!("{}", floor);
}

fn print_field(field: &Vec<Vec<bool>>, floor: usize) {
    for y in 0..=floor.min(10) {
        for x in 0..field[y].len() {
            print!("{}", if field[floor - y][x] { '#' } else { '.' })
        }
        println!(" {}", floor - y);
    }
    println!();
    println!();
}

fn apply_tile(field: &mut Vec<Vec<bool>>, cut: i64, tile: &Tile, x: i64, y: i64) {
    for i in 0..tile.len() as i64 {
        let tile_y = i;
        let field_y = y - i - cut;
        for j in 0..tile[tile_y as usize].len() as i64 {
            let tile_x = j;
            let field_x = x + j;
            if tile[tile_y as usize][tile_x as usize] {
                field[field_y as usize][field_x as usize] = true;
            }
        }
    }
}

fn crashes(field: &Vec<Vec<bool>>, cut: i64, tile: &Tile, x: i64, y: i64) -> bool {
    for i in 0..tile.len() as i64 {
        let tile_y = i;
        let field_y = y as i64 - i as i64 - cut;
        if field_y >= field.len() as i64 {
            panic!("field too small");
        }
        if field_y < 0 {
            return true;
        }
        for j in 0..tile[tile_y as usize].len() as i64 {
            let tile_x = j;
            let field_x = x as i64 + j as i64;
            if field_x < 0 || field_x >= field[field_y as usize].len() as i64 {
                return true;
            }
            if field[field_y as usize][field_x as usize] && tile[tile_y as usize][tile_x as usize] {
                return true;
            }
        }
    }

    return false;
}

type Tile = Vec<Vec<bool>>;

fn parse_tiles() -> Vec<Tile> {
    let mut tiles = vec![];

    for tile in TILES.split("\n\n") {
        tiles.push(tile.lines().map(|l| { l.chars().map(|c| { c == '#' }).collect() }).collect());
    }

    tiles
}
