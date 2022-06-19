import React, { useState, useEffect } from 'react'
import { useHistory } from 'react-router-dom'
import ProductAPI from '../../api/ProductAPI'
import CategoryAPI from '../../api/CategoryAPI'
import { Box, TextField, InputAdornment, Button, Divider, Card, CardContent, Typography, Select, MenuItem, FormControl } from '@mui/material'
import { Search, AddCircle, Download } from '@mui/icons-material';
import ProductsTable from './ProductsTable'
import UnlockAccess from '../../components/roleBasedRender/UnlockAccess'
import { CSVLink } from "react-csv";

const headers = [
    { label: "Tên sản phẩm", key: 'name' },
    { label: "Thương hiệu", key: 'brand' },
    { label: "Loại sản phẩm", key: 'category' },
    { label: "Khối lượng", key: 'weight' },
    { label: "Tồn kho", key: 'stock' },
    { label: "Mô tả", key: 'description' },
    { label: "Ngày tạo", key: 'createdAt' },
];

export default function Products({ setStateAlert }) {
    const history = useHistory();
    const [trigger, setTrigger] = useState(false);
    const [products, setProducts] = useState([]);
    const [variants, setVariants] = useState([]);
    const [categories, setCategories] = useState([]);
    const [totalStorage, setTotalStorage] = useState(0);
    const [activeVariants, setActiveVariants] = useState();
    const [outStockProducts, setOutStockProducts] = useState();

    const [searchInput, setSearchInput] = useState('');
    const [categoryFilter, setCategoryFilter] = useState('');
    const [searchedProducts, setSearchedProducts] = useState([]);
    function getData() {
        ProductAPI.productList()
            .then((pResult) => {
                let reversedResult = pResult.data.reverse();
                setProducts(reversedResult);
                setSearchedProducts(reversedResult);
                history.push('/san-pham');
            })
            .catch(err => {
                setStateAlert({ severity: "error", variant: "filled", open: true, content: "Có lỗi xảy ra" });
                // history.push('/san-pham');
            });
        ProductAPI.getAllVariants()
            .then((vResult) => {
                console.log("🚀 ~ file: Products.jsx ~ line 48 ~ .then ~ vResult", vResult)
                setVariants(vResult.data);
                setTotalStorage(vResult.data.reduce((sum, v) => sum + v.inventoryQuantity, 0));
                setActiveVariants(vResult.data.filter((variant) => variant.sellableStatus === "Co the ban").length);
                setOutStockProducts(vResult.data.filter((variant) => variant.inventoryQuantity === 0).length);
            })
            .catch(err => {
                setStateAlert({ severity: "error", variant: "filled", open: true, content: "Có lỗi xảy ra" });
                history.push('/san-pham');
            });
        CategoryAPI.CategoryList()
            .then((cResult) => {
                console.log("🚀 ~ file: Products.jsx ~ line 60 ~ .then ~ cResult", cResult)
                setCategories(cResult.data);
            })
            .catch(err => {
                setStateAlert({ severity: "error", variant: "filled", open: true, content: "Có lỗi xảy ra" });
                history.push('/san-pham');
            });
    }
    useEffect(() => {
        getData();
    }, [trigger])

    useEffect(() => {
        searchAndFilter()
    }, [categoryFilter, searchInput]);

    const triggerReload = () => {
        setTrigger(!trigger);
    };

    //inputs
    const handleChange = (e) => {
        let value = e.target.value.toLowerCase();
        setSearchInput(value);
        // let input = value.toLowerCase().normalize("NFD").replace(/\p{Diacritic}/gu, "");
        // let result = products.filter(product => product.name.toLowerCase().normalize("NFD").replace(/\p{Diacritic}/gu, "").indexOf(input) >= 0);
        // setSearchedProducts([...result]);
    }

    const handleChangeCategory = (e) => {
        let value = e.target.value;
        setCategoryFilter(value);
        console.log(value);
    }

    const searchAndFilter = () => {
        let categoryFilterValue = categoryFilter;
        let searchValue = searchInput.toLowerCase().normalize("NFD").replace(/\p{Diacritic}/gu, "");
        let result = [];

        if (categoryFilterValue !== "") {
            result = products.filter(product => product.category === categoryFilterValue);
        } else {
            result = products;
        }
        result = result.filter(product => product.name.toLowerCase().normalize("NFD").replace(/\p{Diacritic}/gu, "").indexOf(searchValue) >= 0);
        setSearchedProducts([...result]);
    }

    const handleDeleteProduct = (id) => {
        ProductAPI.deleteProduct(id)
            .then(res => {
                setStateAlert({ severity: "success", variant: "filled", open: true, content: res.data.name + " đã được xóa" });
            })
            .catch(err => {
                setStateAlert({ severity: "error", variant: "filled", open: true, content: err.response.data });
            });
    }

    return (
        <Box className="product-main-content" backgroundColor="#F4F6F8" minHeight="91vh" pt={2} pb={4} px={4}>
            <Box display="flex" flexDirection="column">
                <Box
                    display="flex"
                    justifyContent="space-between"
                    py={2}
                    px={2}
                    backgroundColor="white"
                >
                    <CSVLink
                        data={searchedProducts}
                        headers={headers}
                        filename="Orders.csv"
                        target="_blank"
                        style={{
                            display: "flex",
                            alignItems: "center",
                            textDecoration: "none",
                            color: "black",
                        }}
                    >
                        <Download />
                        Xuất File
                    </CSVLink>
                    <UnlockAccess request={['ADMIN', 'Nhân viên kho']}>
                        <Button
                            variant='contained'
                            sx={{ mr: 2 }}
                            startIcon={<AddCircle />}
                            onClick={() => { history.push('/tao-san-pham') }}
                        >Thêm sản phẩm
                        </Button>
                    </UnlockAccess>
                </Box>
                <Divider />
                <Box py={2} px={2} display="flex" backgroundColor='white'>
                    <TextField
                        placeholder="Tìm kiếm sản phẩm"
                        variant="outlined"
                        size='small'
                        sx={{ mr: 2, flexGrow: 1 }}
                        value={searchInput}
                        onChange={(e) => handleChange(e)}
                        InputProps={{
                            startAdornment: (
                                <InputAdornment position="start">
                                    <Search />
                                </InputAdornment>
                            ),
                        }}></TextField>
                    <FormControl sx={{ minWidth: 150, mr: 2 }}>
                        {/* <InputLabel>Loại sản phẩm</InputLabel> */}
                        <Select
                            value={categoryFilter}
                            size='small'
                            displayEmpty
                            onChange={handleChangeCategory}
                            renderValue={
                                categoryFilter !== "" ? undefined : () => <Typography sx={{ color: "#aaa" }}>Loại sản phẩm</Typography>
                            }
                        >
                            <MenuItem value="">
                                <Typography >Tất cả</Typography>
                            </MenuItem>
                            {categories.map(category => (
                                <MenuItem key={category.id} value={category.name}>
                                    <Typography >{category.name}</Typography>
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Box>
            </Box>
            <Box py={2} px={1} display="flex" justifyContent="space-evenly" backgroundColor='white'>
                <Card className="card-index-product" sx={{
                    borderRadius: '40px',
                    px: 3,
                    width: '20%'
                }}>
                    <CardContent sx={{ textAlign: 'center', py: 1 }}>
                        <Typography variant='h6'>Lượng hàng tồn kho</Typography>
                        <Typography>{totalStorage}</Typography>
                    </CardContent>
                </Card>
                <Card className="card-index-product" sx={{
                    borderRadius: '40px',
                    px: 3,
                    width: '20%'
                }}>
                    <CardContent sx={{ textAlign: 'center', py: 1 }}>
                        <Typography variant='h6'>Tổng số sản phẩm</Typography>
                        <Typography>{products.length}</Typography>
                    </CardContent>
                </Card>
                <Card className="card-index-product" sx={{
                    borderRadius: '40px',
                    px: 3,
                    width: '20%'
                }}>
                    <CardContent sx={{ textAlign: 'center', py: 1 }}>
                        <Typography variant='h6'>Sản phẩm đang bán</Typography>
                        <Typography>{activeVariants}</Typography>
                    </CardContent>
                </Card>
                <Card className="card-index-product" sx={{
                    borderRadius: '40px',
                    px: 3,
                    width: '20%'
                }}>
                    <CardContent sx={{ textAlign: 'center', py: 1 }}>
                        <Typography variant='h6'>Sản phẩm hết hàng</Typography>
                        <Typography>{outStockProducts}</Typography>
                    </CardContent>
                </Card>
            </Box>
            <Box py={2}>
                <ProductsTable products={searchedProducts} handleDeleteProduct={handleDeleteProduct} triggerReload={triggerReload} />
            </Box>
        </Box>
    )
}
