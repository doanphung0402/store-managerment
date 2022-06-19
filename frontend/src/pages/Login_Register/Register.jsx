import React, { useRef, useState, useContext } from 'react'
import "./register.scss";
import { Link, useHistory } from "react-router-dom";
import { AuthContext } from '../../contextAPI/AuthContext';
import { CircularProgress } from "@material-ui/core";
import axios from 'axios';
import { Alert, Snackbar } from '@mui/material';


export default function Register() {
    const [stateAlert, setStateAlert] = useState({ severity: "", variant: "", open: false, content: "" });


    const username = useRef();
    const password = useRef();
    const email = useRef();
    const { user, isFetching, error, dispatch } = useContext(AuthContext);
    const history = useHistory();

    const RegisterHandle = async (e) => {
        e.preventDefault();
        dispatch({ type: "LOGIN_START" });
        const newAdmin = {
            username: username.current.value,
            email: email.current.value,
            password: password.current.value,
            role: "ADMIN"
        }
        if (username.current.value == "" || password.current.value == "" || email.current.value == "") {
            setStateAlert({ severity: "error", variant: "standard", open: true, content: "Yêu cầu điền tên đăng ký, email và mật khẩu" })
        } else {
            try {
                const res = await axios.post("http://localhost:8080/register", newAdmin);
                dispatch({ type: "LOGIN_SUCCESS", payload: res.data })
                history.push("/login");
            } catch (error) {
                history.push("/login");
                setStateAlert({ severity: "error", variant: "filled", open: true, content: "error.response.data" })
                dispatch({ type: "LOGIN_FAILURE", payload: error });
            }
        }
    }

    return (
        <div className="login_page">
            <div className="background">
                <img className='logo_image' src="https://www.sapo.vn/Themes/Portal/Default/StylesV2/images/home/bg-banner-tet-1280.png?v=7" alt="" />
            </div>
            <div className="container">
                {/* <div className="img">
                    <img className='logo_image' src="./images/logo2.PNG" alt="" />
                </div> */}
                <div className="screen">
                    <div className="screen__content">
                        <form onSubmit={RegisterHandle} className="login">
                            <div className="login__field">
                                <i className="login__icon fas fa-user" />
                                <input ref={username} type="text" className="login__input" placeholder="Tên đăng nhập" />
                            </div>
                            <div className="login__field">
                                <i className="login__icon fas fa-envelope" />
                                <input ref={email} type="text" className="login__input" placeholder="Email" />
                            </div>
                            <div className="login__field">
                                <i className="login__icon fas fa-lock" />
                                <input ref={password} type="password" className="login__input" placeholder="Mật khẩu" />
                            </div>
                            <button type='submit' style={{ marginBottom: "1.5em" }} className="button login__submit">
                                {isFetching ? <CircularProgress color="white" size="20px" /> :
                                    <>
                                        <span className="button__text">Đăng ký</span>
                                        <i className="button__icon fas fa-chevron-right" />
                                    </>
                                }
                            </button>
                            <Link to={"/login"} className='back_to_login'><strong>Bạn đã có tài khoản</strong></Link>
                        </form>
                    </div>
                    <div className="screen__background">
                        <span className="screen__background__shape screen__background__shape4" />
                        <span className="screen__background__shape screen__background__shape3" />
                        <span className="screen__background__shape screen__background__shape2" />
                        <span className="screen__background__shape screen__background__shape1" />
                    </div>
                </div>
            </div>
            <Snackbar
                anchorOrigin={{ vertical: "top", horizontal: "center" }}
                open={stateAlert.open}
                autoHideDuration={2000}
                onClose={() => setStateAlert({ ...stateAlert, open: false })}
            >
                <Alert
                    onClose={() => setStateAlert({ ...stateAlert, open: false })}
                    severity={stateAlert.severity}
                    variant={stateAlert.variant}
                    sx={{ width: '100%' }}
                >
                    {stateAlert.content}
                </Alert>
            </Snackbar>
        </div >
    )
}
